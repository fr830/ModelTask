package com.kio.servlet;

import com.kio.dao.BizSystemLogDao;
import com.kio.entity.SystemLog;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * 获取系统日志
 *
 * @author KIO
 */
@WebServlet(name = "GetSystemLog", urlPatterns = "/getSystemLog")
public class GetSystemLog extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String from = request.getParameter("from");
        String timeStamp = request.getParameter("timeStamp");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        if (from == null || from.equals(""))
            from = "0";
        if (timeStamp == null || timeStamp.equals(""))
            timeStamp = format.format(new Date());

        int iFrom;
        try {
            iFrom = Integer.parseInt(from);
        } catch (Exception e) {
            iFrom = 0;
        }
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(timeStamp));
            c.add(Calendar.DAY_OF_MONTH, 1);
            Date begin = c.getTime();
            c.add(Calendar.DAY_OF_MONTH, 1);
            Date end = c.getTime();
            ArrayList<SystemLog> list = BizSystemLogDao.getLog(iFrom, format.format(begin), format.format(end));
            JSONArray result = new JSONArray();
            format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            for (SystemLog s : list) {
                JSONObject item = new JSONObject(s);
                if (s.getsIsSelf() == 1)
                    item.put("sIsSelf", "是");
                else
                    item.put("sIsSelf", "否");
                item.put("iId", s.getsId());
                item.put("sCreateTime", format.format(s.getsCreateTime()));
                item.put("sOperation", s.getsOperation());
                item.put("sSession", s.getsSession());
                result.put(item);
            }
            PrintWriter out = response.getWriter();
            out.write(result.toString());
            out.close();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

}
