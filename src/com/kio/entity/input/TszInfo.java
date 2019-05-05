package com.kio.entity.input;

/**
 * 退水闸信息对象，包括全局id，名称，当前时间点currentTime，当前分水流量值currentFlow，分水计划时间expectTime，分水计划流量expectFlow
 * @author Frank
 *
 */
public class TszInfo {

	private int jctid;
	
	private String jctname;
	
	private int currentTime;

	private float currentFlow;

	private int expectTime;

	private float expectFlow;
	
	
	public TszInfo(int jctid, String jctname, int currentTime, float currentFlow, int expectTime, float expectFlow) {
		super();
		this.jctid = jctid;
		this.jctname = jctname;
		this.currentTime = currentTime;
		this.currentFlow = currentFlow;
		this.expectTime = expectTime;
		this.expectFlow = expectFlow;
	}

	public int getJctid() {
		return jctid;
	}

	public void setJctid(int jctid) {
		this.jctid = jctid;
	}

	public String getJctname() {
		return jctname;
	}

	public void setJctname(String jctname) {
		this.jctname = jctname;
	}
	
	public float getCurrentFlow() {
		return currentFlow;
	}

	public int getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	public void setCurrentFlow(float currentFlow) {
		this.currentFlow = currentFlow;
	}

	public int getExpectTime() {
		return expectTime;
	}

	public void setExpectTime(int expectTime) {
		this.expectTime = expectTime;
	}

	public float getExpectFlow() {
		return expectFlow;
	}

	public void setExpectFlow(float expectFlow) {
		this.expectFlow = expectFlow;
	}
}
