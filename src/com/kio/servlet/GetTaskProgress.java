package com.kio.servlet;

import com.calldll.utils.FileOperation;
import com.kio.dao.BizComputerOutDao;
import com.kio.dao.BizTaskInfoDao;
import com.kio.entity.output.DataEntity;
import com.kio.listener.Init;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

/**
 * 获取任务进度
 *
 * @author KIO
 */
@WebServlet(name = "GetTaskProgress", urlPatterns = "/getTaskProgress")
public class GetTaskProgress extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected synchronized void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        JSONObject result = new JSONObject();
        PrintWriter out = response.getWriter();

        String sTaskCode = request.getParameter("sTaskCode");
        String iTaskType = request.getParameter("iTaskType");

        int from = 0;
        try {
            from = Integer.parseInt(request.getParameter("from"));
        } catch (Exception ignore) {

        }

        if (sTaskCode == null)
            sTaskCode = "";
        if (iTaskType == null)
            iTaskType = "3";


        switch (iTaskType) {
            case "1":
            case "2":
                taskDo(result, sTaskCode, from);
                break;
            case "3":
                HttpSession session = request.getSession();
                int task3progress = (int) session.getAttribute(sTaskCode);
                //模拟进度
                task3progress += Math.random() * 30;
                if (task3progress > 90)
                    task3progress = 90;
                task3Do(result, sTaskCode, request.getSession(), task3progress);
                session.setAttribute(sTaskCode, task3progress);
                break;
            default:
                result.put("code", 2);
                result.put("msg", "请求参数值非法!");
                break;
        }
        out.write(result.toString());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doGet(request, response);
    }

    /**
     * 应急调控模型和正常扩散模型 查询任务输出的方法
     *
     * @param result    响应内容
     * @param sTaskCode 任务ID
     * @param from      最小id
     */
    private void taskDo(JSONObject result, String sTaskCode, int from) {
        //从数据库读取id大于from的数据
        ArrayList<DataEntity> data = BizComputerOutDao.getItems(sTaskCode, from);
        if (data == null || data.size() <= 0) {
            result.put("code", 1);
            result.put("msg", "暂时没有数据可读取!");
        } else {
            String space = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
            JSONArray items = new JSONArray();
            for (DataEntity item : data) {
                String list = item.getList();
                items.put("<label class='received' style='color:#0c0'>" + item.getTime() + "</label>" + space +
                        "<label class='received' style='color:#00c'>" + item.getFileType() + "</label>" + space +
                        list.substring(0, 80) +
                        "..."
                );
            }
            int stamp = data.get(data.size() - 1).getId();
            result.put("stamp", stamp);
            result.put("code", 0);
            result.put("msg", items);
        }
        int pro = BizTaskInfoDao.getIProgress(sTaskCode);
        boolean isEnd = BizTaskInfoDao.isEnd(sTaskCode);
        result.put("progress", pro);
        result.put("isEnd", isEnd);
    }

    private void task3Do(JSONObject result, String sTaskCode, HttpSession session, int progress) {
        // 检查是否存在该编号的输出文件
        String fileName = Init.PARAMETERS.getModel_3_path() + File.separator + "workfile" + sTaskCode + File.separator + "M溯源结果.txt";

        if (FileOperation.fileExisit(fileName)) {
            BizTaskInfoDao.completeTaskInfo(sTaskCode, new Date(), 1, 0, "任务执行完成");
            result.put("code", 0);
            result.put("isEnd", true);
            progress = 100;
            session.removeAttribute(sTaskCode); //移除结束了的任务的进度
            result.put("stamp", 0);
            result.put("msg", FileOperation.readInfoFromFile(fileName));
        } else {
            result.put("code", 1);
            result.put("msg", "没有读取到结果文件!");
            result.put("isEnd", false);
        }
        result.put("progress", progress);
    }

}
