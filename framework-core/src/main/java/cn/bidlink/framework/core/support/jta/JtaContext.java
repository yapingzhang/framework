package cn.bidlink.framework.core.support.jta;

import java.util.Vector;

import javax.jms.Connection;
import javax.jms.Session;

import org.apache.ibatis.session.SqlSession;

/**
 * @description:JTA上下文事务环境
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2013-9-17 下午2:29:28
 */
public class JtaContext {
	
	public static final ThreadLocal<JtaModel> TRANSACTION_LOCAL = new ThreadLocal<JtaModel>();
	
	public static final ThreadLocal<Vector<SqlSession>> CONNECTION_HOLDER = new ThreadLocal<Vector<SqlSession>>();
	
	public static final ThreadLocal<Session> JMS_SESSION_LOCAL = new ThreadLocal<Session>();
	
	public static final ThreadLocal<Connection> JMS_CONNECTION_LOCAL = new ThreadLocal<Connection>();
	


	
}
