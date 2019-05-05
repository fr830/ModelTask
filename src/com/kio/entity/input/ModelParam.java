package com.kio.entity.input;

public class ModelParam {
	
// 第二行三个参数       0  30000  60  1  1
	/**
	 * 模型模拟总时长，单位秒
	 */
	private int lastTime = 30000;
	
	/**
	 * 计算步长
	 */
	private int stepLength = 60;
	
    /**
     * 水质计算方式，1--应急关闸模式下水质计算，2-- 常规情况下的水质计算，对应于input.txt中的第二行(0  600000  600  1  1)最后一个值
     */
	private int processType = 1;
    

	
// 第三行10个参数	  1 	6	98	1000	0	600	  600	0	0	1	
	/**
	 * 污染物类型,  1代表可降解物，2代表不可降解物
	 */
	private int pollutedType = 1;
	
	/**
	 *  发生应急情况的渠段序号
	 */
	private int pollutedJctIndex = 6;
    
    /**
     * 发生应急段的桩号
     */
	private double pollutedPosition = 98;
	
    /**
     * 污染物的质量，单位kg
     */
	private double pollutedQuality = 1000;
    
	/**
	 * 污染物从开始发生到现在的反应时间，单位为分钟，默认值为0
	 */
	private int reactTime = 0;
	
    /**
     * 上游闸门关闭时间，单位分钟
     */
	private double upTime = 600;
    
    /**
     * 下游闸门关闭时间，单位分钟
     */
	private double downTime = 600;
    
    /**
     * 上游下游闸门动作时间间隔，单位分钟, 默认为0
     */
	private double interval = 0;
    
	/**
     * 下游闸门的控制方式, 目前统一设置为0
     */
	private int EmerControlType = 0;
	
	
	/**
     * 运行模式 0--闸前常水位   1--等体积+闸前常水位 
     */
	private int operationMode = 1;


	public int getLastTime() {
		return lastTime;
	}


	public void setLastTime(int lastTime) {
		this.lastTime = lastTime;
	}


	public int getStepLength() {
		return stepLength;
	}


	public void setStepLength(int stepLength) {
		this.stepLength = stepLength;
	}


	public int getProcessType() {
		return processType;
	}


	public void setProcessType(int processType) {
		this.processType = processType;
	}


	public int getPollutedType() {
		return pollutedType;
	}


	public void setPollutedType(int pollutedType) {
		this.pollutedType = pollutedType;
	}


	public int getPollutedJctIndex() {
		return pollutedJctIndex;
	}


	public void setPollutedJctIndex(int pollutedJctIndex) {
		this.pollutedJctIndex = pollutedJctIndex;
	}


	public double getPollutedPosition() {
		return pollutedPosition;
	}


	public void setPollutedPosition(double pollutedPosition) {
		this.pollutedPosition = pollutedPosition;
	}


	public double getPollutedQuality() {
		return pollutedQuality;
	}


	public void setPollutedQuality(double pollutedQuality) {
		this.pollutedQuality = pollutedQuality;
	}


	public int getReactTime() {
		return reactTime;
	}


	public void setReactTime(int reactTime) {
		this.reactTime = reactTime;
	}


	public double getUpTime() {
		return upTime;
	}


	public void setUpTime(double upTime) {
		this.upTime = upTime;
	}


	public double getDownTime() {
		return downTime;
	}


	public void setDownTime(double downTime) {
		this.downTime = downTime;
	}


	public double getInterval() {
		return interval;
	}


	public void setInterval(double interval) {
		this.interval = interval;
	}


	public int getEmerControlType() {
		return EmerControlType;
	}


	public void setEmerControlType(int emerControlType) {
		EmerControlType = emerControlType;
	}


	public int getOperationMode() {
		return operationMode;
	}


	public void setOperationMode(int operationMode) {
		this.operationMode = operationMode;
	}
    
	
}
