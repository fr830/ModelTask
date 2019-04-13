package com.kio.dao;

import java.util.ArrayList;
import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;

import com.kio.entity.SystemLog;
import com.kio.mapper.BizSystemLogMapper;

/**
 * 系统日志DAO层
 *
 * @author KIO
 */
public class BizSystemLogDao {
    /**
     * 插入一条日志
     *
     * @param log 日志实例
     * @return 是否成功
     */
    public static boolean insertOperation(SystemLog log) {
        boolean flag;
        SqlSession session = new DBTools().getNewSession();
        BizSystemLogMapper mapper = session.getMapper(BizSystemLogMapper.class);
        flag = mapper.insertOperation(log);
        session.commit();
        session.close();
        return flag;
    }

    /**
     * 获取序号大于from的日志
     *
     * @param from 从id
     * @return 日志列表
     */
    public static ArrayList<SystemLog> getLog(int from, String begin, String end) {
        ArrayList<SystemLog> list = null;

        SqlSession session = new DBTools().getNewSession();
        BizSystemLogMapper mapper = session.getMapper(BizSystemLogMapper.class);
        try {
            list = mapper.getLog(from, begin, end);
            session.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        }
        return list;
    }
}
