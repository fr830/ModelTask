package com.calldll.inputFileWriter;

import java.io.*;
import java.util.*;

public class FileReadTool {

	private String filename = null;

	public FileReadTool(String filename) {
		this.filename = filename;
	}

	/**
	 * 将文件filename中的文本按行读取到List中
	 * @return 文本行List，每行一个元素
	 */
	public List<String> readFile() {
		List<String> lines = new Vector<String>();
		try {
			//read file content from file
			//FileReader reader = new FileReader(filename);
			InputStreamReader reader = new InputStreamReader(new FileInputStream(filename),"GBK");
			BufferedReader br = new BufferedReader(reader);

			String str = null;

			while ((str = br.readLine()) != null) {
				if (str.trim().length() > 0)
					lines.add(str);
			}

			br.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return lines;
	}
	
	/**
	 * 将多行文本写入到filename文件中
	 * @param filename 待写入的文件名称，包含路径
	 * @param lines 待写入的多行文本
	 * @return 操作成功与否
	 */
	public boolean writeFile(String filename, List<String> lines) {
		boolean result = false;
		OutputStreamWriter writer = null;
		BufferedWriter bw = null;
		try {
			
			//writer = new FileWriter(filename);
			writer = new OutputStreamWriter(new FileOutputStream(filename),"GBK");
	        bw = new BufferedWriter(writer);
	        for(String line : lines) {
	        	bw.write(line);
	        	bw.write("\r\n");
	        }
	        bw.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return result;
	}
	
	/**
	 * java1.6 实现java8中的String.join方法
	 * @param join 中间连接字符串
	 * @param strAry 待连接的字符串数组
	 * @return 连接后字符串
	 */
	 public static String join(String join, String[] strAry){
	        StringBuffer sb=new StringBuffer();
	        for(int i=0;i<strAry.length;i++){
	             if(i==(strAry.length-1)){
	                 sb.append(strAry[i]);
	             }else{
	                 sb.append(strAry[i]).append(join);
	             }
	        }
	        return new String(sb);
	    }
}
