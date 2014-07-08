package cn.bidlink.fileserver.db;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * 
 * @author changzhiyuan
 * @date 2013-2-18
 * @desc ibatis 获取mysession
 * 
 */
public class IbatisSessionFactory {
	// 配置文件
	private static String CONFIG_FILE_LOCATION = "SqlMapper.xml";
	// ThreadLocal存放当前线程中的SqlSession
	private static final ThreadLocal<SqlSession> threadLocal = new ThreadLocal<SqlSession>();
	private static SqlSessionFactory sessionFactory;

	private IbatisSessionFactory() {
	}

	// 获取SqlSession
	public static SqlSession getSession() {
		SqlSession session = (SqlSession) threadLocal.get();

		if (session == null) {
			if (sessionFactory == null) {
				rebuildSessionFactory();
			}
			session = (sessionFactory != null) ? sessionFactory.openSession()
					: null;
			threadLocal.set(session);
		}

		return session;
	}

	// 构建SessionFactory
	public static void rebuildSessionFactory() {
		try {
			Reader reader = Resources.getResourceAsReader(CONFIG_FILE_LOCATION);
			sessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 关闭SqlSession
	public static void closeSession() {
		SqlSession session = (SqlSession) threadLocal.get();
		threadLocal.set(null);

		if (session != null) {
			session.close();
		}
	}

	// 将SessionFactory关闭
	public static void closeSessionFactory() {
		sessionFactory = null;
	}
}