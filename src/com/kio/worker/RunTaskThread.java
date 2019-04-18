package com.kio.worker;

import com.calldll.utils.FileOperation;
import com.calldll.utils.Task3ParameterHelper;
import com.kio.listener.Init;

import java.io.File;

public class RunTaskThread extends Thread {

	private String sTaskCode;
	private String params;
	public RunTaskThread(String sTaskCode, String params) {
		this.sTaskCode = sTaskCode;
		this.params = params;
	}

	@Override
	public void run(){
		try {
			// 删除原先的结果文件
			FileOperation
					.delFile(Init.PARAMETERS.getModel_3_path() + File.separator + "workfile" + sTaskCode + File.separator + "M溯源结果.txt");
			String run = Init.PARAMETERS.getModel_3_path() + File.separator + "workfile" + sTaskCode + File.separator + "SuYuanM.exe";
			Task3ParameterHelper.updateParameter(sTaskCode,params);
			Runtime.getRuntime().exec("cmd /k start " + run, null,
					new File(Init.PARAMETERS.getModel_3_path() + File.separator + "workfile" + sTaskCode));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
