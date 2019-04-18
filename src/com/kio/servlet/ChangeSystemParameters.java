package com.kio.servlet;

import com.kio.entity.SystemParameters;
import com.kio.listener.Init;
import com.kio.worker.TaskPool;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ChangeSystemParameters", urlPatterns = "/changeSystemParameters")
public class ChangeSystemParameters extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String data = request.getParameter("data");

        JSONObject result = new JSONObject();
        result.put("success", false);
        result.put("msg", "系统存在活动线程，不允许修改！");

        if (TaskPool.currentThread() <= 0) {
            try {
                JSONObject dataObject = new JSONObject(data);
                int maxThreadNum = dataObject.getInt("maxThreadNum");
                String path1 = dataObject.getString("model_1_path");
                String path2 = dataObject.getString("model_2_path");
                String path3 = dataObject.getString("model_3_path");
                //改变系统参数
                Init.PARAMETERS = new SystemParameters(
                        maxThreadNum,
                        path1,
                        path2,
                        path3
                );
                result.put("success", true);
                result.remove("msg");

            } catch (JSONException | NullPointerException e) {
                e.printStackTrace();
                result.put("msg", "参数错误！");
            }

        }

        response.getWriter().write(result.toString());
        response.getWriter().close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }
}
