package com.kio.worker;

import com.kio.dao.BizComputerOutDao;
import com.kio.dao.BizTaskInfoDao;
import com.kio.entity.DataEntity;
import net.sf.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
 * 将一条数据插入数据库中，并更新任务进度
 */
public class InsertDataToDB {
	private String uuid;
	private int lastTime;
	private int type;
	
	public InsertDataToDB(){
	}
	
	public InsertDataToDB(String uuid, int type, int lastTime){
		this.uuid = uuid;
		this.type = type;
		this.lastTime = lastTime;
	}
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getLastTime() {
		return lastTime;
	}

	public void setLastTime(int lastTime) {
		this.lastTime = lastTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	/**
	* 将DATAINPUTDyna.dat中的一条数据插入数据库中，更新任务进度
	* @param data, time, progress, end
	*/
	public void databaseDyna(List<List<String>> data, float time, int progress, boolean end)
			throws  ParseException {
		if (!data.isEmpty()) {
			// Insert data into BizComputerOut
			DataEntity dataEntity = new DataEntity();
			dataEntity.setiTaskType(type);
			dataEntity.setsTaskCode(uuid);
			dataEntity.setTime(time);
			dataEntity.setFileType(1);
			dataEntity.setList(JSONArray.fromObject(data).toString());
			data.clear();

			boolean flag = false;
			flag = BizComputerOutDao.insertData(dataEntity);
			if (!flag)
				System.out.print("\n" + uuid + "  Inserted record failed");

			//Update progress
		    flag = false;
		    if(!end)
		    	flag = BizTaskInfoDao.updateIProgress(uuid, progress);
		    else{
		    	flag = true;
		    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    	BizTaskInfoDao.completeTaskInfo(uuid, df.parse(df.format(new Date())), 1, 0, "");
		    }
				
			if(!flag)
				System.out.println(uuid + "\tUpdate progress failed");
		}
	}
	
	/**
	* 将DATAINPUT水质.dat中的一条时间点数据插入数据库中，更新任务进度
	* @param lineData, time, progress, end
	*/
	public void databaseWaterQuality(String lineData, float time, int progress, boolean end)
				throws ParseException {
			if (lineData!="") {
				
				// Insert data into BizComputerOut
				DataEntity dataEntity = new DataEntity();
				dataEntity.setiTaskType(type);
				dataEntity.setsTaskCode(uuid);
				dataEntity.setTime(time);
				dataEntity.setFileType(2);
				dataEntity.setList(lineData);

				boolean flag = false;
				flag = BizComputerOutDao.insertData(dataEntity);
				if (!flag)
					System.out.print("\n" + uuid + "  Inserted record failed");

				//Update progress
			    flag = false;
			    if(!end)
			    	flag = BizTaskInfoDao.updateIProgress(uuid, progress);
			    else{
			    	flag = true;
			    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    	BizTaskInfoDao.completeTaskInfo(uuid, df.parse(df.format(new Date())), 1, 0, "");
			    }
					
				if(!flag)
					System.out.println(uuid + "\tUpdate progress failed");
			}
		}
}
