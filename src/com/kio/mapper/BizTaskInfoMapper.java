package com.kio.mapper;

import org.apache.ibatis.annotations.Param;

import com.kio.entity.task.BizTaskInfo;
/**
 * 任务信息匹配类
 * @author KIO
 *
 */
public interface BizTaskInfoMapper {
	/**
	 * 插入一条任务
	 * @param taskInfo 描述该任务的任务类
	 * @return 是否成功
	 */
	boolean insertTaskInfo(BizTaskInfo taskInfo);
	/**
	 * 更新任务进度
	 * @param taskInfo 任务信息实体类
	 * @return 是否成功
	 */
	boolean updateIProgress(BizTaskInfo taskInfo);
	/**
	 * 只记录任务编号及类型
	 * @param taskInfo 任务信息实体类
	 * @return 是否成功
	 */
	boolean createTaskInfo(BizTaskInfo taskInfo);
	/**
	 * 只记录任务开始执行的时间
	 * @param taskInfo 任务信息实体类
	 * @return 是否成功
	 */
	boolean executeTaskInfo(BizTaskInfo taskInfo);
	/**
	 * 完成任务，更新任务是否错误，是否正常结束等
	 * @param taskInfo 任务信息实体类
	 * @return 是否成功
	 */
	boolean completeTaskInfo(BizTaskInfo taskInfo);
	int getIProgress(@Param("uuid") String uuid);
	int isEnd(String sTaskCode);
	BizTaskInfo getItem(String sTaskCode);
}
