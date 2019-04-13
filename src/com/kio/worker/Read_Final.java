package com.kio.worker;

import com.calldll.inputFileWriter.InputFileWriter;
import com.calldll.utils.BufferedRandomAccessFile;
import com.calldll.utils.FileOperation;
import com.kio.dao.BizTaskInfoDao;
import com.kio.entity.DataCell;
import com.kio.listener.Init;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;

import java.io.*;
import java.text.ParseException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;


/**
 * 读取EXE输出的结果并存入数据库
 * @author KIO
 *
 */
public class Read_Final {

	public MySyn mySyn;
	
	public Read_Final(String uuid, int type, String Info1, String Info2) {
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
	private Map<String, String> Info = new HashMap<>();
	private String Info2 = "";
	private InsertDataToDB insertDataToDB = new InsertDataToDB();

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
	* 动态读取DATAINPUTDyna.dat的数据，并将数据和进度插入数据库中
	* @param filename, UUID, type
	*/
	public int readDyna(String filename, String uuid)
			throws InterruptedException, ParseException {
		BufferedRandomAccessFile rf = null; 
		try {
			String line; 
			long len = 0; 
			float time = 0; 
			rf = new BufferedRandomAccessFile(filename + File.separator + "DATAINPUTDyna.dat", "r"); 
			List<List<String>> dataList = new ArrayList<>(); 
			Instant lastIns = Instant.now();
			
			while ( ((line = rf.readLine()) !=  null) || (time*3600+0.1 < lastTime)) {
				long newlen = rf.length(); 
				if (newlen > len) {
					rf.seek(len); // 将文件指针指向上次读取完后的位置
					line = rf.readLine(); 
					while (line !=  null) {
						lastIns = Instant.now();
						line = new String(line.getBytes("ISO-8859-1"), "gbk");
						String[] str = line.trim().split("\\s+"); 
						
						//将读取到的每行数据放入DataCell（data11）实体中
						if (str.length == 3) {
							insertDataToDB.databaseDyna(dataList, time, false);
							time = Float.parseFloat(str[1]); 
						} else if (str.length == 5 || str.length == 7 || str.length == 10){
							DataCell dataCell = new DataCell(); 
							dataCell.setTime(time); 
							dataCell.setAllData(str); 
							dataList.add(dataCell.getAllData());
						}
						
						line = rf.readLine();
					} //while
					
					newlen = rf.length(); 
					len = newlen;
					Thread.sleep(400);
				} else {
					//无法读取到dat文件的新行数，exe运行速度慢 或 运行出错
					if((Duration.between(lastIns, Instant.now()).getSeconds() < 10)){
						System.out.println("Thread 2000");
						Thread.sleep(2000);
					} else {
						System.out.println("Timeout Error!");
						return 2;
					}
				}
			} //while
			insertDataToDB.databaseDyna(dataList, time, true);
			
			//等待DATAINPUT水质.dat、DATAINPUTSteady.dat不再增长后，关闭exe的运行
			FileOperation.waitFileComplete(filename + File.separator + "DATAINPUT水质.dat");
			FileOperation.waitFileComplete(filename + File.separator + "DATAINPUTSteady.dat");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rf != null)
					rf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		p.destroy();
		System.err.println(uuid + " : Destroy process!");
		return 0;
	}
	

	/**
	* 动态读取DATAINPUT水质.dat的数据，并将数据和进度插入数据库中
	* @param filename, UUID, type
	*/
	public int readWaterQuality(String filename, String uuid)
			throws InterruptedException, ParseException {
		BufferedRandomAccessFile rf = null; 
		try {
			String line; 
			long len = 0; 
			float time = 0; 
			String lineData = "";
			String[] str = null;
			rf = new BufferedRandomAccessFile(filename + File.separator + "DATAINPUT水质.dat", "r"); 
			Instant lastIns = Instant.now();
			
			while ( ((line = rf.readLine()) !=  null) || (time*3600+0.1 < lastTime)) {
				long newlen = rf.length(); 
				if (newlen > len) {
					rf.seek(len); 	// 将文件指针指向上次读取完后的位置
					line = rf.readLine(); 
					while (line !=  null) {
						
						lastIns = Instant.now();
						str = line.trim().split("\\s+");
						
						if (str.length == 3) {	//73
							System.out.println(lineData);
							if(!lineData.equals("×®ºÅ,ÊØºã;"))	//
								insertDataToDB.databaseWaterQuality(lineData, time, false);
							lineData = "";
							time = Float.parseFloat(str[1]); 
						} else if(str.length == 10) {	//134
							if((!str[1].equals("0.00000")) && (!str[1].equals("-0.00000")))
								lineData += str[0] + "," + str[1] + ";";
						}
						
						line = rf.readLine();
					} //while
					
					newlen = rf.length(); 
					len = newlen;
					Thread.sleep(400);
				} else {
					//无法读取到dat文件的新行数，exe运行速度慢 或 运行出错
					if((Duration.between(lastIns, Instant.now()).getSeconds() < 10)){
						System.out.println("Thread 2000");
						Thread.sleep(2000);
					} else {
						System.out.println("Timeout Error!");
						return 2;
					}
				}
			} //while
			insertDataToDB.databaseWaterQuality(lineData, time, true);
			
			//等待DATAINPUTDyna.dat、DATAINPUTSteady.dat不再增长后，关闭exe的运行
			FileOperation.waitFileComplete(filename + File.separator + "DATAINPUTDyna.dat");
			FileOperation.waitFileComplete(filename + File.separator + "DATAINPUTSteady.dat");
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rf != null)
					rf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		p.destroy();
		System.err.println(uuid + " : Destroy process!");
		return 0;
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
		try {

			String currentPath = Init.PARAMETERS.getModel_1_path() + File.separator + "workfile" + uuid;
			System.out.println("Type:" + type + "\tTaskcode" + uuid);
			
			createParameters();		//暂时模拟固定参数与实时参数，应急模型参数
			
			BizTaskInfoDao.executeTask(uuid, new Date());
			lastTime = Integer.parseInt(Info.get("lastTime"));		//模拟时长
			InputFileWriter.fillParam(uuid, 2, Info, Info2);		//替换参数
			boolean flag = openTask(currentPath + File.separator + "Test.exe");
			
			insertDataToDB.setLastTime(lastTime);
			insertDataToDB.setType(type);
			insertDataToDB.setUuid(uuid);

			if(!flag)
				System.out.println("\n"+ uuid + "  Fail to execute exe!");
			else {
				switch (type) {
				case 1:
					flag = FileOperation.checkFileExist(currentPath + File.separator + "DATAINPUT水质.dat");
					if(flag)
						readWaterQuality(currentPath, uuid);	//DATAINPUT水质.dat存在，开始读取数据
					else
						System.out.println("\n"+ uuid + "  Fail to find DATAINPUT水质.dat!");
					break;
					
				case 2:
					flag = FileOperation.checkFileExist(currentPath + File.separator + "DATAINPUTDyna.dat");
					if(flag)
						readDyna(currentPath, uuid);	//DATAINPUTDyna.dat存在，开始读取数据
					else
						System.out.println("\n"+ uuid + "  Fail to find DATAINPUTDyna.dat!");
					break;
					
				default:
					break;
				}
			}
		} catch (Exception ignored) {

		}
	}

}
