package com.kio.mapper;

import java.util.ArrayList;

import com.kio.entity.SystemLog;
/**
 * 系统日志匹配类
 * @author KIO
 *
 */
public interface BizSystemLogMapper {
	/**
	 * 插入操作
	 * @param log 描述该操作的实体类
	 * @return 是否成功
	 */
	boolean insertOperation(SystemLog log);
	/**
	 * 获取编号大于from的系统日志
	 * @param from 编号
	 * @return 系统日志的列表
	 */
	ArrayList<SystemLog> getLog(int from,String begin,String end);
}
