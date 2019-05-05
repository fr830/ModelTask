package com.kio.entity.task;

/**
 * 进度类，将Dyna线程和WaterQuality线程中更慢的进度作为任务进度
 */
public class ReadFileProgress {
	private int progress = 0;		//任务进度
	private int progressDyna = 0;	//Dyna线程读取进度
	private int progressWater = 0;	//WaterQuality线程读取进度

	public int getProgress() {
		return progress;
	}

	public void setProgress() {		//将Dyna线程和WaterQuality线程中更慢的进度作为任务进度
		this.progress = (progressDyna < progressWater)?progressDyna:progressWater;
	}

	public void setProgressDyna(int progressDyna) {
		this.progressDyna = progressDyna;
		setProgress();
	}

	public void setProgressWater(int progressWater) {
		this.progressWater = progressWater;
		setProgress();
	}
}
