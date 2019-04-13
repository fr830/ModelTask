package com.calldll.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class FileOperation {

	/**
	 * @param 文件内容清空，如没有则创建文件
	 * @return
	 */
	public static void clearOrCreateInfoForFile(String fileName) {
		File file = new File(fileName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write("");
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param 读取文件内容，文件中的内容为String
	 * @return
	 */
	public static List<String> readInfoFromFile(String fileName) {
		// fileName =
		// "D:\\Users\\Kio\\eclipse-workspace\\Model\\readDataFile\\src\\com\\nchu\\resource\\DATAINPUTDyna.dat";
		File file = new File(fileName);

		if (!file.exists()) {
			return null;
		}
		//System.out.println(file.length());
		List<String> resultStr = new ArrayList<String>();
		try {
			//System.out.println(file.length());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "gbk"));
			String str = null;
			while (null != (str = bufferedReader.readLine())) {
				//System.out.print(file.length());
				resultStr.add(str);
				//System.out.println(str);
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultStr;
	}

	/**
	 * @param 在已有的文件后面追加信息
	 * @param info
	 */
	public static void appendInfoToFile(String fileName, String info) {
		File file = new File(fileName);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fileWriter = new FileWriter(file, true);
			info = info + System.getProperty("line.separator");
			fileWriter.write(info);
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 复制文件夹
	 * 
	 * @param sourcePath 源
	 * @param newPath    新
	 * @throws IOException
	 */
	public static void copyDir(String sourcePath, String newPath) throws IOException {
		File file = new File(sourcePath);
		String[] filePath = file.list();
		if (!(new File(newPath)).exists()) {
			(new File(newPath)).mkdir();
		}

		for (int i = 0; i < filePath.length; i++) {
			
			if ((new File(sourcePath + File.separator + filePath[i])).isDirectory()) {
				copyDir(sourcePath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
			}

			if (new File(sourcePath + File.separator + filePath[i]).isFile()) {
				copyFile(sourcePath + File.separator + filePath[i], newPath + File.separator + filePath[i]);
			}

		}
	}

	
	/**
	 * 拷贝文件
	 * 
	 * @param oldPath
	 * @param newPath
	 * @throws IOException
	 */
	public static void copyFile(String oldPath, String newPath) throws IOException {
		File oldFile = new File(oldPath);
		File file = new File(newPath);
		FileInputStream in = new FileInputStream(oldFile);
		FileOutputStream out = new FileOutputStream(file);

		byte[] buffer = new byte[2097152];
		int readByte = 0;
		while ((readByte = in.read(buffer)) != -1) {
			out.write(buffer, 0, readByte);
		}

		in.close();
		out.close();
	}

	
	/***
	 * 删除指定文件夹下所有文件
	 * 
	 * @param path 文件夹完整绝对路径
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}

	
	/***
	 * 删除文件夹
	 * 
	 * @param folderPath文件夹完整绝对路径
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static boolean fileExisit(String filePath) {
		return new File(filePath).exists();
	}

	
	public static void delFile(String filePath) {
		File myFile = new File(filePath);
		if (myFile.exists() && myFile.isFile()) {
			myFile.delete();
		}
	}
	
	
	public static void reNameFile(String filePath, String newName) {
		File myFile = new File(filePath);
		if (myFile.exists() && myFile.isFile()) {
			myFile.renameTo(new File(newName));
		}
	}
	
	/**
	* 等待动态文件停止增长
	* @param filePath
	*/
	public static void waitFileComplete(String filePath){
		try {
			RandomAccessFile file = new RandomAccessFile(filePath, "r"); 
			while(true){
				long fileSize = file.length();
				Thread.sleep(2000);
				if((file.length() == fileSize) && (fileSize > 0)){
					System.err.println("\n" + filePath + " is stop growing up");
					break;
				}
				else
					System.out.println("\n" + filePath + " is still growing up");
			}
			file.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("waitFileComplete Error: " + filePath);
			e.printStackTrace();
		}
	}
	
	/**
	* 5次检测文件存在
	* @param filePath
	*/
	public static boolean checkFileExist(String filePath){
		boolean flag = false;
		try {
			int i = 0;
			//使用5次缓冲时间等待文件生成
			while(true){
				File datFile = new File(filePath);
				if(datFile.exists()){
					flag = true; break;
				} else {
					if(i>5){
						flag = false; break;
					}
				    System.out.println("\nwait "+ 500*i +"ms for " + filePath);
					Thread.sleep(500*(++i));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("checkFileExist Error: " + filePath);
			e.printStackTrace();
		}
		return flag;
	}
}
