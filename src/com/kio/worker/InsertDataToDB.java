package com.kio.worker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.kio.dao.BizComputerOutDao;
import com.kio.dao.BizTaskInfoDao;
import com.kio.entity.DataEntity;

import net.sf.json.JSONArray;

public class InsertDataToDB {
	private String uuid;
	private int lastTime;
	private int type;
	
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
	* 将DATAINPUTDyna.dat中的一条数据以及一次进度值插入数据库中
	* @param data, time, end
	*/
	public void databaseDyna(List<List<String>> data, float time, boolean end)
			throws InterruptedException, ParseException {
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
		    int progress = (int)(((time*3600)/lastTime)*100);
		    if(!end)
		    	flag = BizTaskInfoDao.updateIProgress(uuid, progress);
		    else{
		    	flag = true;
		    	progress = 100;
		    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    	BizTaskInfoDao.completeTaskInfo(uuid, df.parse(df.format(new Date())), 1, 0, "");
		    }
				
			if(!flag)
				System.out.println(uuid + "\tUpdate progress failed");
		}
	}
	
	/**
	* 将DATAINPUT水质.dat中的一条时间点数据以及一次进度值插入数据库中
	* @param lineData, time, end
	*/
	public void databaseWaterQuality(String lineData, float time, boolean end)
				throws InterruptedException, ParseException {
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
			    int progress = (int)(((time*3600)/lastTime)*100);
			    if(!end)
			    	flag = BizTaskInfoDao.updateIProgress(uuid, progress);
			    else{
			    	flag = true;
			    	progress = 100;
			    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			    	BizTaskInfoDao.completeTaskInfo(uuid, df.parse(df.format(new Date())), 1, 0, "");
			    }
					
				if(!flag)
					System.out.println(uuid + "\tUpdate progress failed");
			}
		}
}
