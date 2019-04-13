package com.kio.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;

import org.json.JSONObject;

import com.calldll.utils.FileOperation;
import com.calldll.utils.UUIDUtil;
import com.kio.dao.BizTaskInfoDao;
import com.kio.listener.Init;

/**
 * 创建任务API
 * 
 * @author KIO
 *
 */
@WebServlet(name = "CreateTask", urlPatterns = "/createTask")
public class CreateTask extends javax.servlet.http.HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(javax.servlet.http.HttpServletRequest request,
			javax.servlet.http.HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();

		JSONObject result = new JSONObject();// 响应
		String uuid = UUIDUtil.getUUID();// 获取随机的任务编号
		String type = request.getParameter("iTaskType");

		if (type == null)
			type = "3";

		switch (type) {
		case "3":
			task3Do(result, uuid);
			break;
		case "2":
			task2Do();
			break;
		case "1":
			task1Do(result, uuid);
			break;
		default:
			result.put("code", 2);
			result.put("msg", "请求参数值非法!");
		}

		out.write(result.toString());
		out.close();
	}

	private void task2Do() {

	}

	private void task1Do(JSONObject result, String uuid) {
		try {
			FileOperation.copyDir(Init.PARAMETERS.getModel_1_path() + File.separator + "sourcefile",
					Init.PARAMETERS.getModel_1_path() + File.separator + "workfile" + uuid);

			if (BizTaskInfoDao.createTask(uuid, 1)) {
				result.put("code", 0);
				result.put("msg", uuid);
			} else {
				result.put("code", 3);
				result.put("msg", "数据库错误!");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* 模型三 */
	private void task3Do(JSONObject result, String uuid) {
		try {
			FileOperation.copyDir(Init.PARAMETERS.getModel_3_path() + File.separator + "sourcefile",
					Init.PARAMETERS.getModel_3_path() + File.separator + "workfile" + uuid);

			if (BizTaskInfoDao.createTask(uuid, 3)) {
				result.put("code", 0);
				result.put("msg", uuid);
			} else {
				result.put("code", 3);
				result.put("msg", "数据库错误!");
			}

		} catch (Exception e) {
			result.put("code", 1);
			result.put("msg", "系统无法创建运行空间!");
		}
	}

	protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
			throws IOException {
		doPost(request, response);
	}

}
