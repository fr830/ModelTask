package com.calldll.inputFileWriter;

import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.calldll.utils.FileOperation;
import com.kio.entity.input.FskInfo;
import com.kio.entity.input.JzzInfo;
import com.kio.entity.input.ModelParam;
import com.kio.entity.input.TszInfo;
import com.kio.listener.Init;


public class InputFileWriter {
	
	/**
	 * 根据模型任务号、类别、固定参数和实施参数，
	 * @param uuid -- 模型任务号
	 * @param type -- 任务类型，1代表水动力正常扩散模型，2代表水动力应急模型
	 * @param Info1 -- 固定参数的键值对
	 * @param Info2 -- 实时参数，每个参数以","分隔，每行参数以";"分隔， 例如"4,3,肖楼分水口,0,0,3600,0;14,9,刁河退水闸,0,0,3600,0;"
	 * @return 操作成功与否 
	 * @throws Exception 
	 */
	public static boolean fillParam(String uuid, int type, Map<String, String> Info1, String Info2) {
		String currentPath = Init.PARAMETERS.getModel_1_path() + File.separator + "workfile" + uuid;
		String inputFile = currentPath + File.separator + "DATAINPUT.txt";
		String outputFile = currentPath + File.separator + "DATAINPUT_new1.txt";
		
		ModelParam param = null;
		ModelInputFileWriter writer = null;
		param = mockModelParam(Info1);	//将键值对转化为固定参数对象
		
		if(type==1){	//水动力正常扩散模型
			writer = new NormalModelInputFileWriter(inputFile);
		} else {		//水动力应急调控模型
			writer = new EmergentModelInputFileWriter(inputFile);
		}
		
		//将String类型的实时参数放入不同的List中
		String[] strList = Info2.split(";");
		List<String> jzzList = new ArrayList<>();
		List<String> fskList = new ArrayList<>();
		List<String> tszList = new ArrayList<>();
		for(int i=0; i<strList.length; i++){
			switch (strList[i].substring(0, 2)) {
			case "1,": jzzList.add(strList[i]); break;	//给定水位
			case "4,": fskList.add(strList[i]); break;	//分水口
			case "6,": jzzList.add(strList[i]); break;	//节制闸
			case "14": tszList.add(strList[i]); break;	//退水闸
			default: break;
			}
		}
		//将List转化为对应的数据对象映射表
		Map<String, JzzInfo> jzzMap = mockJzzInfo(jzzList);
		Map<String, FskInfo> fskMap = mockFskInfo(fskList);
		Map<String, TszInfo> tszMap = mockTszInfo(tszList);

		try {
			if (writer.composeInputFile(param, jzzMap, fskMap, tszMap, outputFile)) {
				FileOperation.delFile(inputFile);
				FileOperation.reNameFile(outputFile, inputFile);
				System.out.println("运行完成！");
			} else {
				System.out.println("运行出错，请查看日志");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	
	private static ModelParam mockModelParam(Map<String, String> Info) {
		ModelParam param = new ModelParam();
		param.setLastTime(Integer.parseInt(Info.get("lastTime")));
		param.setStepLength(Integer.parseInt(Info.get("stepLength")));
		param.setProcessType(Integer.parseInt(Info.get("processType")));
		param.setPollutedType(Integer.parseInt(Info.get("pollutedType")));
		param.setPollutedJctIndex(Integer.parseInt(Info.get("pollutedJctIndex")));
		param.setPollutedPosition(Double.parseDouble(Info.get("pollutedPosition")));
		param.setPollutedQuality(Double.parseDouble(Info.get("pollutedQuality")));
		if(param.getProcessType()==1){
			param.setReactTime(Integer.parseInt(Info.get("reactTime")));
			param.setUpTime(Double.parseDouble(Info.get("upTime")));
			param.setDownTime(Double.parseDouble(Info.get("downTime")));
			param.setInterval(Double.parseDouble(Info.get("interval")));
			param.setEmerControlType(Integer.parseInt(Info.get("EmerControlType")));
			param.setOperationMode(Integer.parseInt(Info.get("operationMode")));
		}
		
		return param;
	}
	
	//将List转换为退水闸数据对象映射表
	private static Map<String, TszInfo> mockTszInfo(List<String> tszList) {
		Map<String, TszInfo> tszInfo = new HashMap<String, TszInfo>();
		for(int i=0; i<tszList.size(); i++){
			String[] list = tszList.get(i).split(",");
			tszInfo.put(list[1],new TszInfo(Integer.parseInt(list[1]),list[2], Integer.parseInt(list[3]), 
					Float.parseFloat(list[4]), Integer.parseInt(list[5]), Float.parseFloat(list[6])));
		}
		return tszInfo;
	}
	
	//将List转换为分水口数据对象映射表
	private static Map<String, FskInfo> mockFskInfo(List<String> fskList) {
		Map<String, FskInfo> fskInfo = new HashMap<String, FskInfo>();
		for(int i=0; i<fskList.size(); i++){
			String[] list = fskList.get(i).split(",");
			fskInfo.put(list[1],new FskInfo(Integer.parseInt(list[1]),list[2], Integer.parseInt(list[3]),
					Float.parseFloat(list[4]), Integer.parseInt(list[5]), Float.parseFloat(list[6])));
		}
		return fskInfo;
	}
	
	//将List转换为节制闸数据对象映射表
	private static Map<String, JzzInfo> mockJzzInfo(List<String> jzzList) {
		Map<String, JzzInfo> jzzInfo = new HashMap<String, JzzInfo>();
		for(int i=0; i<jzzList.size(); i++){
			String[] list = jzzList.get(i).split(",");
			jzzInfo.put(list[1],new JzzInfo(Integer.parseInt(list[1]),list[2], 
					Float.parseFloat(list[3]), Float.parseFloat(list[4]), Float.parseFloat(list[5])));
		}
		return jzzInfo;
	}
}
