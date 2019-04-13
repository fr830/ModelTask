package com.calldll.inputFileWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class testInputFileWrite {
	//测试替换
	//两者只可运行其一
	public static void main(String[] args) {
		testEmergentModel();
		//testNormalModel();
	}
	
	public static void testNormalModel() {
		String uuid = "1";
		int type = 1;
		
		Map<String, String> Info1 = new HashMap<String, String>();
		Info1.put("lastTime", "40000");
		Info1.put("stepLength", "300");
		Info1.put("processType", "2");
		Info1.put("pollutedType", "1");
		Info1.put("pollutedJctIndex", "6");
		Info1.put("pollutedPosition", "98");
		Info1.put("pollutedQuality", "1000");
		
		String Info2 = "";
		//normal.txt为水动力正常扩散时，退水闸、分水口、节制闸和给定水位的实时参数信息
		try (FileReader reader = new FileReader("E:\\迅雷下载\\Model0926\\sourcefile1\\normal.txt"); 
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

		InputFileWriter.fillParam(uuid, type, Info1, Info2);
	}
	
	public static void testEmergentModel() {
		String uuid = "1";
		int type = 2;
		
		Map<String, String> Info1 = new HashMap<String, String>();
		Info1.put("lastTime", "30000");
		Info1.put("stepLength", "60");
		Info1.put("processType", "1");
		Info1.put("pollutedType", "1");
		Info1.put("pollutedJctIndex", "6");
		Info1.put("pollutedPosition", "98");
		Info1.put("pollutedQuality", "1000");
		Info1.put("reactTime", "0");
		Info1.put("upTime", "600");
		Info1.put("downTime", "600");
		Info1.put("interval", "0");
		Info1.put("EmerControlType", "0");
		Info1.put("operationMode", "1");
		
		String Info2 = "";
		//emergent.txt为水动力应急扩散时，退水闸、分水口、节制闸和给定水位的实时参数信息
		try (FileReader reader = new FileReader("E:\\迅雷下载\\Model0926\\sourcefile1\\emergent.txt"); 
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

		InputFileWriter.fillParam(uuid, type, Info1, Info2);
	}
}
