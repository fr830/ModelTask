package com.kio.worker;

import com.calldll.utils.BufferedRandomAccessFile;
import com.kio.entity.DataCell;
import com.kio.entity.ReadFileProgress;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取文件数据、插入数据库
 * 使用ReadDyna和ReadWaterQuality两个线程同时读取文件
 */
public class ReadFile {
	private ReadDyna readDyna;
	private ReadWaterQuality readWaterQuality;
	
	
	public ReadFile(String filename, String uuid, int type, int lastTime, ReadFileProgress rfProgress){
		this.readDyna = new ReadDyna(filename, uuid, type, lastTime, rfProgress);
		this.readWaterQuality = new ReadWaterQuality(filename, uuid, type, lastTime, rfProgress);
	}
	
	//启动ReadDyna和ReadWaterQuality两个线程
	public void startRead() {
		Thread t1 = new Thread(readDyna);
		Thread t2 = new Thread(readWaterQuality);
		t1.start();
		t2.start();
	}
}


/**
 * ReadDyna线程读取DATAINPUTDyna.dat文件，将数据插入数据库，同时更新任务进度
 */
class ReadDyna implements Runnable{
	private String filename;
	private int lastTime;
	private ReadFileProgress rfProgress;
	private InsertDataToDB insertDataToDB;
	
	
	public ReadDyna(String filename, String uuid, int type, int lastTime, ReadFileProgress rfProgress) {
		this.filename = filename;
		this.lastTime = lastTime;
		this.rfProgress = rfProgress;
		this.insertDataToDB = new InsertDataToDB(uuid, type, lastTime);
	}
	
	
	@Override
	public void run() {
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
						
						//在下次读取到时间点时，将上次的时间点及数据插入数据库
						if (str.length == 3) {		//时间点
							//利用进度类通信，将Dyna线程和WaterQuality线程中更慢的进度作为实际任务进度
							int progressDyna = (int)( ((time*3600)/lastTime)*100 );
							rfProgress.setProgressDyna(progressDyna);
							insertDataToDB.databaseDyna(dataList, time, rfProgress.getProgress(), false);
							
							time = Float.parseFloat(str[1]); 
							
						} else if (str.length == 5 || str.length == 7 || str.length == 10){		//有效数据
							//将读取到的每行数据放入DataCell实体中，对str的拆分与拼接
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
					}
				}
			} //while
			//最后一次的数据
			rfProgress.setProgressDyna(100);
			insertDataToDB.databaseDyna(dataList, time, rfProgress.getProgress(), true);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭对文件的使用
			try {
				if (rf != null)
					rf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}


/**
 * ReadWaterQuality线程读取DATAINPUT水质.dat文件，将数据插入数据库，同时更新任务进度
 */
class ReadWaterQuality implements Runnable{
	private String filename;
	private int lastTime;
	private ReadFileProgress rfProgress;
	private InsertDataToDB insertDataToDB;
	
	
	public ReadWaterQuality(String filename, String uuid, int type, int lastTime, ReadFileProgress rfProgress) {
		this.filename = filename;
		this.lastTime = lastTime;
		this.rfProgress = rfProgress;
		this.insertDataToDB = new InsertDataToDB(uuid, type, lastTime);
	}
	
	
	@Override
	public void run() {
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
						
						//在下次读取到时间点时，将上次的时间点及数据插入数据库
						if (str.length == 3) {	//时间点
							//当lineData不为"桩号,守恒;"时，由于未对文件编码进行设置，这里直接判断不为"×®ºÅ,ÊØºã;"
							if(!lineData.equals("×®ºÅ,ÊØºã;")){
								//利用进度类通信，将Dyna线程和WaterQuality线程中更慢的进度作为实际任务进度
								int progressWater = (int)( ((time*3600)/lastTime)*100 );
								rfProgress.setProgressWater(progressWater);
								insertDataToDB.databaseWaterQuality(lineData, time, rfProgress.getProgress(), false);
							}
							lineData = "";
							time = Float.parseFloat(str[1]); 
						} else if(str.length == 10) {	//有效数据的长度特征
							if((!str[1].equals("0.00000")) && (!str[1].equals("-0.00000")))
								lineData += str[0] + "," + str[1] + ";";	//将不为0的数据拼接
						}
						
						line = rf.readLine();
					} //while
					
					newlen = rf.length(); 
					len = newlen;
				} else {
					//无法读取到dat文件的新行数，exe运行速度慢 或 运行出错
					if((Duration.between(lastIns, Instant.now()).getSeconds() < 10)){
						System.out.println("Thread 2000");
						Thread.sleep(2000);
					} else {
						System.out.println("Timeout Error!");
					}
				}
			} //while
			//最后一次的数据
			rfProgress.setProgressWater(100);
			insertDataToDB.databaseWaterQuality(lineData, time, rfProgress.getProgress(), true);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//关闭对文件的使用
			try {
				if (rf != null)
					rf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
