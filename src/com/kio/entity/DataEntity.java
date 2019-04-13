package com.kio.entity;
/**
 * 结果行
 * @author KIO
 *
 */
public class DataEntity {
	private int id;
	private float time;
	private String list;
	private String sTaskCode;
	private int iTaskType;

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	private int fileType;

	public int getiTaskType() {
		return iTaskType;
	}

	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}

	public int getITaskType() {
		return iTaskType;
	}

	public void setiTaskType(int iTaskType) {
		this.iTaskType = iTaskType;
	}

	public String getsTaskCode() {
		return sTaskCode;
	}

	public void setsTaskCode(String sTaskCode) {
		this.sTaskCode = sTaskCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
