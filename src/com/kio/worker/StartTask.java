package com.kio.worker;

import com.calldll.inputFileWriter.InputFileWriter;
import com.calldll.utils.FileOperation;
import com.kio.entity.ReadFileProgress;
import com.kio.listener.Init;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * 读取EXE输出的结果并存入数据库
 * @author KIO
 *
 */
public class StartTask {

	public MySyn mySyn;
	
	public StartTask(String uuid, int type, String Info1, String Info2) {
		this.mySyn =  new MySyn(uuid, type, Info1, Info2);
		/*
		 * 该类开辟了新的线程并做了以下工作 1.文件拷贝-已一处 2.执行exe模型 3.一边读取程序输出的文件，一边向数据库插入运行结果
		 */
	}
	
	public Runnable getRunnable() {
		return this.mySyn;
	}
}


/**
 * 启动EXE模型，并向数据库写入数据
 * @author KIO
 *
 */
class MySyn implements Runnable {
	private Process p = null; 
	private String uuid;
	private int lastTime;
	private int type;
	private String Info1 = "";
	private Map<String, String> Info = new HashMap<String, String>();
	private String Info2 = "";

	public MySyn(String uuid, int type, String Info1, String Info2) {
		this.uuid = uuid;
		this.type = type;
		this.Info1 = Info1;
		this.Info2 = Info2;
	}

	
	/**
	 * 启动tese.exe
	 *
	 * @param path
	 * @return Error:false, Work properly:true
	 * @throws IOException, NullPointerException
	 */
	private boolean openTask(String path) {
		try {
			p = Runtime.getRuntime().exec(path); 
			Thread.sleep(2000);

			//第一个参数是Windows窗体的窗体类，第二个参数是窗体的标题。
			HWND hwnd = User32.INSTANCE.FindWindow(null,  path);
			if (hwnd != null) {
				System.out.println("Error: The hwnd of the massagebox is " + hwnd.toString());
				System.out.println("Stopping exe application...");
				p.destroy();
				return false;
			} else {
				System.out.println("\n" + path + "  EXE application start working...");
			}
			
		} catch (Exception e) {
			System.out.println("Error exec!");
			return false;
		}
		return true;
	}
	
	
	/**
	* 暂时模拟固定参数与实时参数的
	*/
	public void createParameters() {
		
		Info.put("lastTime", "30000");
		Info.put("stepLength", "60");
		Info.put("processType", "1");
		Info.put("pollutedType", "1");
		Info.put("pollutedJctIndex", "6");
		Info.put("pollutedPosition", "98");
		Info.put("pollutedQuality", "1000");
		Info.put("reactTime", "0");
		Info.put("upTime", "600");
		Info.put("downTime", "600");
		Info.put("interval", "0");
		Info.put("EmerControlType", "0");
		Info.put("operationMode", "1");
		
		String pathEmergent = Init.PARAMETERS.getModel_1_path() + File.separator + "workfile" + uuid +  File.separator + "emergent.txt";
		//emergent.txt为水动力应急扩散时，退水闸、分水口、节制闸和给定水位的实时参数信息
		try (FileReader reader = new FileReader(pathEmergent); 
			BufferedReader br = new BufferedReader(reader)
		) { 
			String line; 
			while ((line = br.readLine()) != null) {
				Info2 += line; 
			} 
		} catch (IOException e) {
			e.printStackTrace(); 
		}
		System.out.println(Info2);
	}
	
	
	@Override
	public void run() {
		String currentPath = Init.PARAMETERS.getModel_1_path() + File.separator + "workfile" + uuid;
		System.out.println("Type:" + type + "\tTaskcode" + uuid);
		
		try {
			createParameters();		//暂时模拟固定参数与实时参数，应急模型参数
			lastTime = Integer.parseInt(Info.get("lastTime"));		//模拟时长
			InputFileWriter.fillParam(uuid, 2, Info, Info2);		//替换参数
			boolean flag = openTask(currentPath + File.separator + "Test.exe");

			if(!flag)
				System.out.println("\n"+ uuid + "  Fail to execute exe!");
			else {
				//（5次）判断DATAINPUTDyna.dat和DATAINPUT水质.dat文件的存在
				boolean flag1 = FileOperation.checkFileExist(currentPath + File.separator + "DATAINPUTDyna.dat");
				boolean flag2 = FileOperation.checkFileExist(currentPath + File.separator + "DATAINPUT水质.dat");
				
				//文件都存在后，启动两个线程分别读取文件
				if(flag1 && flag2){
					ReadFileProgress rfProgress = new ReadFileProgress();
					ReadFile readFile = new ReadFile(currentPath, uuid, type, lastTime, rfProgress);
					readFile.startRead();
				} else {
					if(!flag1) System.out.println("\n"+ uuid + "  Fail to find DATAINPUTDyna.dat!");
					if(!flag2) System.out.println("\n"+ uuid + "  Fail to find DATAINPUT水质.dat!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//等待DATAINPUTDyna.dat和DATAINPUT水质.dat文件停止增长后，关闭线程
			FileOperation.waitFileComplete(currentPath + File.separator + "DATAINPUTDyna.dat");
			FileOperation.waitFileComplete(currentPath + File.separator + "DATAINPUT水质.dat");
			
			p.destroy();
			System.err.println(uuid + " : Destroy process!");
		}
	}

}
