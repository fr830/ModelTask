package com.kio.entity;

import java.util.Date;

/**
 * 系统日志实体类
 * @author KIO
 *
 */
public class SystemLog {
	@Override
	public String toString() {
		return "SystemLog [sOperation=" + sOperation + ", sCreateTime=" + sCreateTime + ", sSession=" + sSession
				+ ", sIsSelf=" + sIsSelf + "]";
	}
	private String sOperation;//操作
	private Date sCreateTime;//创建时间
	private String sSession;//用户的SessionID
	/**
	 * 是否本机，1是，0否
	 */
	private int sIsSelf;
	/**
	 * 日志编号，插入时不需要
	 */
	private int sId;
	
	public String getsOperation() {
		return sOperation;
	}
	public void setsOperation(String sOperation) {
		this.sOperation = sOperation;
	}
	public Date getsCreateTime() {
		return sCreateTime;
	}
	public void setsCreateTime(Date sCreateTime) {
		this.sCreateTime = sCreateTime;
	}
	public String getsSession() {
		return sSession;
	}
	public void setsSession(String sSession) {
		this.sSession = sSession;
	}
	public int getsIsSelf() {
		return sIsSelf;
	}
	public void setsIsSelf(int sIsSelf) {
		this.sIsSelf = sIsSelf;
	}
	public int getsId() {
		return sId;
	}
	public void setsId(int sId) {
		this.sId = sId;
	}
	
}
