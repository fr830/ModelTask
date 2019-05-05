package com.kio.dao;

import java.util.Date;
import org.apache.ibatis.session.SqlSession;

import com.kio.entity.task.BizTaskInfo;
import com.kio.mapper.BizTaskInfoMapper;

/**
 * 任务DAO层
 * 
 * @author KIO
 *
 */
public class BizTaskInfoDao {

	/**
	 * 在数据库中创建一个任务
	 * 
	 * @param uuid
	 *            任务ID
	 * @param type
	 *            任务类型
	 * @return 是否成功
	 */
	public static boolean createTask(String uuid, int type) {
		boolean flag = false;
		SqlSession session = new DBTools().getNewSession();
		BizTaskInfoMapper mapper = session.getMapper(BizTaskInfoMapper.class);
		try {
			BizTaskInfo task = new BizTaskInfo();
			task.setsTaskCode(uuid);
			task.setiTaskType(type);
			flag = mapper.createTaskInfo(task);

			session.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return flag;
	}

	/**
	 * 执行任务，将会插任务开始执行时间
	 * 
	 * @param uuid 任务编号
	 * @param creatTime 创建时间
	 * @return 是否成功
	 */
	public static boolean executeTask(String uuid, Date creatTime) {
		boolean flag = false;
		SqlSession session = new DBTools().getNewSession();
		BizTaskInfoMapper mapper = session.getMapper(BizTaskInfoMapper.class);
		try {
			BizTaskInfo task = new BizTaskInfo();
			task.setsTaskCode(uuid);
			task.settCreatTime(creatTime);
			flag = mapper.executeTaskInfo(task);

			session.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return flag;
	}

	/**
	 * 完成任务，将会填入结束时间，是否错误，是否结束，以及描述
	 * 
	 * @param uuid 任务编号
	 * @param endTime 结束时间
	 * @param isEnd 是否结束
	 * @param isError 是否出错
	 * @param taskInfo 留言
	 * @return 是否成功
	 */
	public static boolean completeTaskInfo(String uuid, Date endTime, int isEnd, int isError, String taskInfo) {
		boolean flag = false;
		SqlSession session = new DBTools().getNewSession();
		BizTaskInfoMapper mapper = session.getMapper(BizTaskInfoMapper.class);
		try {
			BizTaskInfo task = new BizTaskInfo();

			task.setsTaskCode(uuid);
			task.settEndTime(endTime);
			task.setbIsEnd(isEnd);
			task.setbIsError(isError);
			task.setsTaskInfo(taskInfo);

			flag = mapper.completeTaskInfo(task);

			session.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return flag;
	}

	/**
	 * 从数据库获取任务进度
	 * 
	 * @param uuid
	 *            任务编号
	 * @return 任务进度
	 */
	public static int getIProgress(String uuid) {
		int result = 0;
		SqlSession session = new DBTools().getNewSession();
		BizTaskInfoMapper mapper = session.getMapper(BizTaskInfoMapper.class);
		try {
			result = mapper.getIProgress(uuid);
			System.out.println(uuid + " get(Dao) " + result + "  " + Thread.currentThread());
			session.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}

	/**
	 * 更新任务进度
	 * 
	 * @param uuid
	 *            任务编号
	 * @param progress
	 *            任务进度
	 * @return 是否成功
	 */
	public static boolean updateIProgress(String uuid, int progress) {
		SqlSession session = new DBTools().getNewSession();
		BizTaskInfoMapper mapper = session.getMapper(BizTaskInfoMapper.class);
		try {
			BizTaskInfo bizTaskInfo = new BizTaskInfo();
			bizTaskInfo.setsTaskCode(uuid);
			bizTaskInfo.setiProgress(progress);
			boolean flag = mapper.updateIProgress(bizTaskInfo);
			session.commit();
			session.close();
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}

		return false;
	}

	/**
	 * 返回任务是否已结束
	 * 
	 * @param sTaskCode
	 *            任务编号
	 * @return 是否结束
	 */
	public static boolean isEnd(String sTaskCode) {
		int result = 0;
		SqlSession session = new DBTools().getNewSession();
		BizTaskInfoMapper mapper = session.getMapper(BizTaskInfoMapper.class);
		try {
			result = mapper.isEnd(sTaskCode);
			session.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result == 1;
	}

	/**
	 * 获取完整的任务信息
	 * 
	 * @param sTaskCode 任务编号
	 * @return 该编号的任务
	 */
	public static BizTaskInfo getItem(String sTaskCode) {
		BizTaskInfo result = null;
		SqlSession session = new DBTools().getNewSession();
		BizTaskInfoMapper mapper = session.getMapper(BizTaskInfoMapper.class);
		try {
			result = mapper.getItem(sTaskCode);
			session.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return result;
	}

}
