package com.kio.entity;

/**
 * 节制闸信息对象，包括全局编号jctid，名称jctname，闸前水位frontwl，闸后水位backwl, 过闸流量afterflow
 * @author Frank
 *
 */
public class JzzInfo {
	private int jctid;
	
	private String jctname;
	
	private float frontwl;
	
	private float backwl;
	
	private float afterflow;
	
	public JzzInfo(int jctid, String jctname, float frontwl, float backwl, float afterflow) {
		super();
		this.jctid = jctid;
		this.jctname = jctname;
		this.frontwl = frontwl;
		this.backwl = backwl;
		this.afterflow = afterflow;
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

	public float getFrontwl() {
		return frontwl;
	}

	public void setFrontwl(float frontwl) {
		this.frontwl = frontwl;
	}

	public float getBackwl() {
		return backwl;
	}

	public void setBackwl(float backwl) {
		this.backwl = backwl;
	}

	public float getAfterFlow() {
		return afterflow;
	}

	public void setAfterFlow(float afterflow) {
		this.afterflow = afterflow;
	}
}
