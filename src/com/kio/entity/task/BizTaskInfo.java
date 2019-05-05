package com.kio.entity.task;

import java.util.Date;

/**
 * 任务实体类
 * @author KIO
 *
 */
public class BizTaskInfo {
	private String sTaskCode;//任务编码
	private int iTaskType;//任务类型
	private Date tCreatTime;//任务创建时间
	private Date tEndTime;//任务结束时间
 	private String sFixedParam;//固定参数：Json子串
	private String sRTimeParam;//实时参数：Json子串
	private int iProgress;//进度：1-100
	private int bIsEnd;//是否完成：1是；0否
	private int bIsError;
	private String sTaskInfo;
	public String getsTaskCode() {
		return sTaskCode;
	}
	public void setsTaskCode(String sTaskCode) {
		this.sTaskCode = sTaskCode;
	}
	public int getiTaskType() {
		return iTaskType;
	}
	public void setiTaskType(int iTaskType) {
		this.iTaskType = iTaskType;
	}
	public Date gettCreatTime() {
		return tCreatTime;
	}
	public void settCreatTime(Date tCreatTime) {
		this.tCreatTime = tCreatTime;
	}
	public Date gettEndTime() {
		return tEndTime;
	}
	public void settEndTime(Date tEndTime) {
		this.tEndTime = tEndTime;
	}
	public String getsFixedParam() {
		return sFixedParam;
	}
	public void setsFixedParam(String sFixedParam) {
		this.sFixedParam = sFixedParam;
	}
	public String getsRTimeParam() {
		return sRTimeParam;
	}
	public void setsRTimeParam(String sRTimeParam) {
		this.sRTimeParam = sRTimeParam;
	}
	public int getiProgress() {
		return iProgress;
	}
	public void setiProgress(int iProgress) {
		this.iProgress = iProgress;
	}
	public int isbIsEnd() {
		return bIsEnd;
	}
	public void setbIsEnd(int bIsEnd) {
		this.bIsEnd = bIsEnd;
	}
	public int isbIsError() {
		return bIsError;
	}
	public void setbIsError(int bIsError) {
		this.bIsError = bIsError;
	}
	public String getsTaskInfo() {
		return sTaskInfo;
	}
	public void setsTaskInfo(String sTaskInfo) {
		this.sTaskInfo = sTaskInfo;
	}
	

}
