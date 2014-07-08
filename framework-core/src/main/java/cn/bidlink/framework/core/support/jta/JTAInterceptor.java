package cn.bidlink.framework.core.support.jta;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Vector;

import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import cn.bidlink.framework.core.annotation.JTATransaction;
import cn.bidlink.framework.core.exceptions.BidJmsException;


/**
 * @description: JTA事务支持
 * @since Ver 1.0
 * @author <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date 2012 2012-8-24 下午10:51:54
 */
@Aspect
@Component
@Order(0)
public class JTAInterceptor {

	private static final Logger logger = Logger.getLogger(JTAInterceptor.class);

	@Around(value = "execution(* *cn.bidlink..service..impl.*ServiceImpl.* (..))", argNames = "pjp")
	public Object doException(ProceedingJoinPoint pjp) throws Throwable {
		Object obj = null;
		Method method = null;
		JtaModel model = null;
		JTATransaction jtaTran = null;
		try {
			Signature sign = pjp.getSignature();
			if (sign instanceof MethodSignature) {
				MethodSignature methodSign = (MethodSignature) sign;
				method = methodSign.getMethod();
				jtaTran = method.getAnnotation(JTATransaction.class);
				if (jtaTran != null) {
					if (JtaContext.TRANSACTION_LOCAL.get() == null) { // 只控制最外层
						JtaContext.TRANSACTION_LOCAL.set(new JtaModel(System.currentTimeMillis(), method));
						JtaContext.CONNECTION_HOLDER.set(new Vector<SqlSession>());
					}
				}
			}

			obj = pjp.proceed();

			model = JtaContext.TRANSACTION_LOCAL.get();
			if (jtaTran != null) {
				if (method != null && model != null && method.hashCode() == model.getMethod().hashCode()) {
 					commitAllTransaction();
				}
			}

		} catch (Throwable e) {
			if (jtaTran != null) {
				rollbackAll();
			}
			throw e;
		} finally { 
			model = JtaContext.TRANSACTION_LOCAL.get();
			if (jtaTran != null && method != null && model != null && method.hashCode() == model.getMethod().hashCode()) {
				removeCurThreadLocal(model);
			}

		}
		return obj;
	}

	/**
	 * @description  清空线程绑定
	 * @param model
	 */
	private void removeCurThreadLocal(JtaModel model) {
		try {
			JtaContext.TRANSACTION_LOCAL.remove();
			JtaContext.CONNECTION_HOLDER.remove();
			JtaContext.JMS_SESSION_LOCAL.remove();
			JtaContext.JMS_CONNECTION_LOCAL.remove();
			logger.debug("grobal trancation cost time :" + (System.currentTimeMillis() - model.getStartTime()) +"ms");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * @description 提交事务
	 */
	private void commitAllTransaction() {
 
		//[1]提交数据库事务
		Vector<SqlSession> vectorList = JtaContext.CONNECTION_HOLDER.get();
		if (vectorList != null && vectorList.size() > 0) {
			if (executeTest(vectorList)) {
				for (SqlSession sqlSession : vectorList) {
					setAutoCommit(sqlSession.getConnection(), true);
					commit(sqlSession);
				}
			} else {
				for (SqlSession sqlSession : vectorList) {
					rollback(sqlSession);
				}
			}
		}
		
		//[2]提交JMS事务
		Session session = JtaContext.JMS_SESSION_LOCAL.get();
		javax.jms.Connection jmsCon = JtaContext.JMS_CONNECTION_LOCAL.get();
		if(session != null) {
			commitIfNecessary(session);
		}
		if(jmsCon != null) {
			closeJmsConnection(jmsCon);
		}
		
	}

	
	private void commitIfNecessary(Session session){
		Assert.notNull(session, "Session must not be null");
		try {
			session.commit();
		}catch (Exception ex) {
              throw new BidJmsException(ex);
		} finally {
			try {
				session.close();
			} catch (JMSException e) {
				 throw new BidJmsException(e);
			}
		}
	}
	
	private void rollbackIfNecessary(Session session) {
		Assert.notNull(session, "Session must not be null");
 		try {
			session.rollback();
		}catch (Exception ex) {
			 throw new BidJmsException(ex);
		} finally {
			try {
				session.close();
			} catch (JMSException e) {
				 throw new BidJmsException(e);
			}
		}
	}
	
	private void closeJmsConnection(javax.jms.Connection connection) {
		Assert.notNull(connection, "jms connection must not be null");
		try {
			connection.close();
		} catch (JMSException e) {
			throw new BidJmsException(e);
		}
	}
	
	
	/**
	 * @description 回滚所有操作
	 */
	private void rollbackAll() {
		//【1】回滚数据库
		Vector<SqlSession> vectorList = JtaContext.CONNECTION_HOLDER.get();
		if (vectorList != null && vectorList.size() > 0) {
			for (SqlSession sqlSession : vectorList) {
				rollback(sqlSession);
			}
		}
		
		//【2】JMS回滚
		Session session = JtaContext.JMS_SESSION_LOCAL.get();
		javax.jms.Connection jmsCon = JtaContext.JMS_CONNECTION_LOCAL.get();
		if(session != null) {
			rollbackIfNecessary(session);
		}
		if(jmsCon != null) {
			closeJmsConnection(jmsCon);
		}
				
				
	}

	/**
	 * @description 提交
	 * @param con
	 */
	private void commit(SqlSession sqlSession) {
		try {
			sqlSession.commit(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

		} finally {
			sqlSession.close();
		}
	}

	/**
	 * @description 回滚
	 * @param con
	 */
	private void rollback(SqlSession sqlSession) {
		try {
			sqlSession.rollback(true);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			sqlSession.close();
		}
	}

	/**
	 * @description 测试连接是否可用
	 * @param vectorList
	 * @return true 表示成功 | false 失败回滚
	 */
	private boolean executeTest(Vector<SqlSession> vectorList) {
		if (vectorList == null) {
			return false;
		}
		for (SqlSession session : vectorList) {
			if (!checkConnection(session)) {
				return false; // 只要一个连接不成功所有就失败
			}
		}
		return true;
	}

	private boolean checkConnection(SqlSession session) {
		try {
			Connection con = session.getConnection();
			Statement stmt = con.createStatement();
			stmt.executeQuery("select 'x' ");
			stmt.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	private void setAutoCommit(Connection con, boolean autoCommit) {
		try {
			if (con != null)
				con.setAutoCommit(autoCommit);
		} catch (Exception e) {}
	}
}
