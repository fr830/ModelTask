package com.kio.worker;

import com.kio.listener.Init;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 解析数据线程池-Read_Final
 * 控制该线程的执行数
 * @author KIO
 *
 */
public class ReadFinalPool {
	public static int maxQueue = 20;	//最大缓冲队列
	public static ExecutorService fixPool = Executors.newFixedThreadPool(Init.PARAMETERS.getMaxThreadNum());	//固定式线程池
	public static ThreadPoolExecutor tpe = ((ThreadPoolExecutor) fixPool);		//监测线程池信息
	public static int perTaskTime = 120;	//预计每个任务所需运行时间

	public static int getMaxQueue() {
		return maxQueue;
	}

	public static void setMaxQueue(int maxQueue) {
		ReadFinalPool.maxQueue = maxQueue;
	}

	public static int getPerTaskTime() {
		return perTaskTime;
	}

	public static void setPerTaskTime(int perTaskTime) {
		ReadFinalPool.perTaskTime = perTaskTime;
	}

	//缓冲队列大小
	private static int queueTask() {
    	return tpe.getQueue().size();
	}
    
	//当前活跃线程
    public static int currentThread() {
		 return tpe.getActiveCount();
	}
    
    //总任务数
    public static long taskCount() {
    	return tpe.getTaskCount();
	}
    
    //已完成任务数
    public static long completedTaskCount() {
		return tpe.getCompletedTaskCount();
	}
    
    //添加新任务预计等待时间
    public static long WaitTime() {
    	if(Init.PARAMETERS.getMaxThreadNum()-currentThread() > 0)
    		return 0;
    	else 
    		return (queueTask()+1)*perTaskTime;
	}
    
    //添加新任务
    /**
     * 
     * @param uuid 任务编号
     * @return <strong>0</strong> execute now,<strong>1</strong> execute delay,<strong>2</strong> can't execute
     */
	public static int addTask(String uuid, int type, String Info1, String Info2){

		if(currentThread()<Init.PARAMETERS.getMaxThreadNum()) {
			fixPool.execute(new Read_Final(uuid, type, Info1, Info2).getRunnable());
			return 0;	//立即执行
		}
		else if(tpe.getQueue().size()<maxQueue){
			fixPool.execute(new Read_Final(uuid, type, Info1, Info2).getRunnable());
			return 1;	//线程池缓冲队列
		}
		else
			return 2;	//拒绝执行

	}
    
    //安全关闭线程池
    public static void shutDown() {
		fixPool.shutdown();
	}
    
    //强制关闭线程池
    public static void shutDownNow() {
		fixPool.shutdownNow();
	}
}