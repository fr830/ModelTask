package com.kio.worker;

import java.io.File;
import com.calldll.utils.FileOperation;
import com.kio.listener.Init;

public class RunTaskThread extends Thread {

	private String sTaskCode;
	
	public RunTaskThread(String sTaskCode) {
		this.sTaskCode = sTaskCode;
	}

	@Override
	public void run(){
		try {
			// 删除原先的结果文件
			FileOperation
					.delFile(Init.PARAMETERS.getModel_3_path() + File.separator + "workfile" + sTaskCode + File.separator + "M溯源结果.txt");
			String run = Init.PARAMETERS.getModel_3_path() + File.separator + "workfile" + sTaskCode + File.separator + "SuYuanM.exe";
			Runtime.getRuntime().exec("cmd /k start " + run, null,
					new File(Init.PARAMETERS.getModel_3_path() + File.separator + "workfile" + sTaskCode));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
