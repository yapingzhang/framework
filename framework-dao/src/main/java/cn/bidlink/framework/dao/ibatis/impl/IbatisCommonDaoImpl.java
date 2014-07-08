package cn.bidlink.framework.dao.ibatis.impl;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import cn.bidlink.framework.core.commons.Paging;
import cn.bidlink.framework.dao.ibatis.resultHandler.CountResultHandler;


/**
 * 这是一个CommonDao的Ibatis实现类，使用了MyBtatis（原来叫Ibatis）框架
 * 实现了框架中的CommonDao接口，对于一些在MyBatis框架中不适应的方法，有 的进行了模拟，有的直接抛出异常，表示该方法不允许调用。
 * 
 * @author Wubing Yang
 * 
 */
public class IbatisCommonDaoImpl {

	private static Logger logger = Logger.getLogger(IbatisCommonDaoImpl.class);

	// ibatis的sqlsession.
	private SqlSession sqlSession;

	// 正在调用的本类中方法的方法对象，通过该对象得到映射的对应的MyBatis映射文件的statement id.
	private Method invokingMethod;

	private Object target;

	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public Method getInvokingMethod() {
		return invokingMethod;
	}

	public void setInvokingMethod(Method invokingMethod) {
		this.invokingMethod = invokingMethod;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	/**
	 * 获得当前方法对应的语句映射Id,如果提供的id不为空， 则直接返回，否则则按照默认规则生成的id.
	 * 
	 * @param statement
	 * @return
	 */
	private String getStatment(String statement) {
		if (org.apache.commons.lang.StringUtils.isEmpty(statement))
			return this.getStatement();
		return statement;
	}

	/**
	 * 获得调用的方法映射的sql语句的statement id。 规约是映射文件的namespace就是对应的dao的类名（类的全名，如String，
	 * 则类名是java.lang.String），映射的statement id是方法名。
	 * 
	 * @return
	 */
	public String getStatement() {
		if (target == null || invokingMethod == null)
			return null;
		Class<?> c = target.getClass();
		Method[] methods = c.getMethods();
		String namespace = target.getClass().getName();
		String id = invokingMethod.getName();
		for (Method m : methods) {
			if (m.getName().equals(invokingMethod.getName())
					&& !m.equals(invokingMethod)) {
				Class<?>[] s = m.getParameterTypes();
				if (s != null && s.length > 0) {
					for (Class<?> z : s) {
						id = id + "_" + z.getSimpleName();
					}
				}
			}
		}
		return namespace + "." + id;
	}
	
	

	/**
	 * 通用的分页查询方法，查询的sql语句是在映射文件中映射的语句。
	 * 
	 * @param statement         映射语句的id.
	 * @param parameter	           查询的参数。
	 * @param paging 分页对象
	 * @Param  isTotalCount 是否进行总页数查询 
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> findPagingList(String statement, Object parameter,Paging paging,boolean isTotalCount) {
		RowBounds rowBounds = new RowBounds(paging.getFirstIndex(),paging.getPageSize());
		//查询出总当前结果
		List<T>  result = sqlSession.selectList(statement,parameter,rowBounds);
		paging.setResult(result);
		//是否需要查询总共有多少条 
		if(isTotalCount==true){
			CountResultHandler handler = new CountResultHandler();
			//执行总页数查询
			sqlSession.select(statement, parameter, handler);
			paging.setRecordTotal(handler.getCount());
			
		}	
		return result;
	}

	/**
	 * 通用的分页查询方法，查询的sql语句是在映射文件中映射的语句。
	 * 
	 * @param statement
	 *            映射语句的id.
	 * @param parameter
	 *            查询的参数。
	 * @param paging
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> findPagingList(String statement, Object parameter,Paging paging) {
			return this.findPagingList(statement, parameter, paging,true);
		
	}
	
	public <T> List<T> findPagingList1(String statement, Object parameter,
			Paging paging) {
		return this.findPagingList(statement, parameter, paging);
	}

//	/**
//	 * 获取记录总数
//	 * 
//	 * @param sql
//	 * @param parameter
//	 * @throws SQLException
//	 */
//	private int getRecordTotal(String sql) throws SQLException {
//		String countSql = getCountSql(sql);
//		if (countSql == null || countSql.length() == 0)
//			return 0;
//		QueryRunner queryRunner = new QueryRunner();
//		Number recordTotal = (Number) queryRunner.query(
//				this.sqlSession.getConnection(), countSql, new ScalarHandler());
//		if (recordTotal == null || recordTotal.longValue() <= 0) {
//			return 0;
//		}
//		return recordTotal.intValue();
//	}

	private static String getCountSql(String sql) {
		String countSql = "";
		if (StringUtils.hasText(sql)) {
			String tempQuery = sql.replaceAll("[f|F][r|R][o|O][m|M]", "FROM");
			countSql = "SELECT COUNT(*) "
					+ tempQuery.substring(tempQuery.indexOf("FROM"));
		}
		return countSql;
	}

	

}


