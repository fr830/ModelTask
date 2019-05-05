package com.kio.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSession;

import com.kio.entity.output.DataEntity;
import com.kio.mapper.DataEntityMapper;
/**
 * 计算结果DAO层
 * @author KIO
 *
 */
public class BizComputerOutDao {
	/**
	 * 通过UUID和开始序号获取完整的结果行
	 * @param uuid 任务ID
	 * @param from 开始序号
	 * @return 返回大于该序号且为该UUID任务的列表
	 */
	public static ArrayList<DataEntity> getItems(String uuid, int from) {
		ArrayList<DataEntity> items = null;
		SqlSession session = new DBTools().getNewSession();
		DataEntityMapper mapper = session.getMapper(DataEntityMapper.class);
		try {
			items = mapper.getItems(uuid, from);
			session.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return items;
	}
	/**
	 * 删除一个任务的结果
	 * @param uuid 需要删除的任务结果
	 * @return <code>true</code>删除成功，<code>false</code>删除失败
	 */
	public static boolean deleteDataEntity(String uuid) {
		boolean flag = false;
		SqlSession session = new DBTools().getNewSession();
		DataEntityMapper mapper = session.getMapper(DataEntityMapper.class);
		try {
			flag = mapper.deleteDataEntity(uuid);
			session.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return flag;
	}
	/**
	 * 通过UUID获取所有的记录
	 * @param uuid 任务ID
	 * @return 返回该UUID任务的列表
	 */
	public static ArrayList<DataEntity> getAllRecords(String uuid) {
		ArrayList<DataEntity> records = null;
		SqlSession session = new DBTools().getNewSession();
		DataEntityMapper mapper = session.getMapper(DataEntityMapper.class);
		try {
			records = mapper.getAllRecords(uuid);
			session.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return records;
	}
	/**
	 * 插入一条结果行
	 * @param dataEntity 结果集
	 * @return <code>true</code>插入成功，<code>插入失败</code>
	 */
	public static boolean insertData(DataEntity dataEntity) {
		SqlSession session = new DBTools().getNewSession();
		DataEntityMapper mapper = session.getMapper(DataEntityMapper.class);
		try {
			boolean flag = mapper.insertAllData(dataEntity);
			session.commit();
			session.close();
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
		return false;
	}
	
}
