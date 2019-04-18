package com.kio.servlet;

import com.calldll.utils.FileOperation;
import com.kio.dao.BizComputerOutDao;
import com.kio.dao.BizTaskInfoDao;
import com.kio.entity.BizTaskInfo;
import com.kio.listener.Init;
import com.kio.worker.TaskPool;
import com.kio.worker.RunTaskThread;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * 执行任务API
 *
 * @author KIO
 */
@WebServlet(name = "ExecuteTask", urlPatterns = "/executeTask")
public class ExecuteTask extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        JSONObject result = new JSONObject();// 响应结果

        String sTaskCode = request.getParameter("sTaskCode");// 任务编号
        String iTaskType = request.getParameter("iTaskType");// 任务类型
        String params = request.getParameter("param");//任务参数

        if (sTaskCode == null)
            sTaskCode = "";
        if (iTaskType == null)
            iTaskType = "3";
        BizTaskInfo taskInfo = BizTaskInfoDao.getItem(sTaskCode);
        if (taskInfo == null) {
            result.put("code", 1);
            result.put("msg", "该任务不存在，无法运行!");
        } else if (taskInfo.gettCreatTime() != null && taskInfo.isbIsEnd() == 0 && taskInfo.isbIsError() == 0) {
            result.put("code", -1);
            result.put("msg", "不允许重复执行，请耐心等待任务执行结束");
        } else {
            switch (iTaskType) {
                case "1":
                case "2":
                    taskDo(sTaskCode, result);
                    break;
                case "3":
                    task3Do(sTaskCode, result, request.getSession(), params);
                    break;
                default:
                    result.put("code", 2);
                    result.put("msg", "请求参数值非法！");
                    break;
            }

        }
        out.write(result.toString());
        out.close();
    }

    private void taskDo(String sTaskCode, JSONObject result) {
        if (FileOperation.fileExisit(Init.PARAMETERS.getModel_1_path() + File.separator + "workfile" + sTaskCode)) {
            BizComputerOutDao.deleteDataEntity(sTaskCode);// 先删除已有的任务结果
            BizTaskInfoDao.updateIProgress(sTaskCode, 0);
            String Info1 = "";//固定参数
            String Info2 = "";//实时参数
            int state = TaskPool.addTask(sTaskCode, 2, Info1, Info2);// 启动数据写入线程
            if (state == 0) {
                result.put("code", 0);
                result.put("msg", "任务正在执行!");
            } else if (state == 1) {// 任务被放入缓冲队列，提示滞后执行
                result.put("code", 0);
                result.put("msg", "当前服务器繁忙，您的任务将被滞后执行，请耐心等候!");
            } else {
                result.put("code", 5);
                result.put("msg", "当前执行任务数过多，请稍后重试!");
            }

        } else {
            result.put("code", 4);
            result.put("msg", "该任务不存在，无法运行!");
        }
    }


    private void task3Do(String sTaskCode, JSONObject result, HttpSession session, String params) {
        Thread task;
        if (FileOperation.fileExisit(Init.PARAMETERS.getModel_3_path() + File.separator + "workfile" + sTaskCode)) {
            task = new RunTaskThread(sTaskCode, params);
            task.start();
            if (BizTaskInfoDao.executeTask(sTaskCode, new Date())) {
                session.setAttribute(sTaskCode, 0);
                result.put("code", 0);
                result.put("msg", "任务正在执行!");
            } else {
                result.put("code", 3);
                result.put("msg", "数据库错误!");
            }

        } else {
            result.put("code", 1);
            result.put("msg", "该任务不存在，无法运行!");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        doGet(request, response);
    }

}
