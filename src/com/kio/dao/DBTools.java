package com.kio.dao;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
/**
 * 数据库工具类
 * @author KIO
 *
 */
class DBTools {

	/**
	 * 获取可单独使用的Session
	 * @return 新的SqlSession
	 */
	SqlSession getNewSession() {
		Reader reader=null;
		try {
			reader = Resources.getResourceAsReader("./com/kio/dao/mybatis.cfg.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		return sessionFactory.openSession();
	}
}
