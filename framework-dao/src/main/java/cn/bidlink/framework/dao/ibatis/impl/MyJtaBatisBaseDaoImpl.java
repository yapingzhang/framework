package cn.bidlink.framework.dao.ibatis.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.bidlink.framework.core.exceptions.BusinessException;
import cn.bidlink.framework.core.support.jta.JtaContext;
import cn.bidlink.framework.core.utils.pager.Pager;
import cn.bidlink.framework.dao.dataSource.DynamicDataSource;
import cn.bidlink.framework.dao.ibatis.MyBatisBaseDao;

 


 
/**
 * 
 * @description: mybatis DAO 通用工具类
 * @version  Ver 1.0
 * @since    Ver 1.0
 * @author   <a href="mailto:dejian.liu@ebnew.com">dejian.liu</a>
 * @Date	 2012	2012-8-3		下午4:40:35
 *
 */
//@Component(value="myJtaBatisBaseDaoImpl")
public class MyJtaBatisBaseDaoImpl<T, PK extends Serializable>  implements  MyBatisBaseDao<T, PK> {
 
    private static Logger logger = Logger.getLogger(MyJtaBatisBaseDaoImpl.class);
    
//    @Autowired
 		private SqlSessionFactory sqlSessionFactory;
 		
// 	    @Autowired
 		private SqlSessionTemplate sqlSessionTemplate;
 		
 		/**
 		 * 插入
 		 */
 		public String INSERT = ".insert";
 		
 		/**
 		 * 批量插入
 		 */
 		public String INSERT_BATCH = ".insertBatch";

 		/**
 		 * 更新
 		 */
 		public String UPDATE = ".update";

 		/**
 		 * 根据ID 删除
 		 */
 		public String DELETE = ".delete";

 		/**
 		 * 根据ID 查询
 		 */
 		public String GETBYID = ".getById";

 		/**
 		 * 根据条件 分页查询
 		 */
 		public String COUNT = ".findPage_count";
 		/**
 		 * 根据条件 分页查询
 		 */
 		public String PAGESELECT = ".findPage";
 		
 		private Object target;
 		
 		private Method invokingMethod;
 		
 	 
 	 
 		@Override
 		public int save(T object) {
 			if(object == null) {
 				throw new BusinessException(" object can't null!");
 			}
 			if(JtaContext.TRANSACTION_LOCAL.get() != null) {
 				SqlSession session = sqlSessionFactory.openSession(false);
 				setAutoCommit(session.getConnection(), false);
 				JtaContext.CONNECTION_HOLDER.get().add(session);
 				 return session.insert(object.getClass().getName()+ INSERT,object);
 			} else {
 				return  sqlSessionTemplate.insert(object.getClass().getName()+ INSERT,object);
 			}
 		}

 		@SuppressWarnings("unchecked")
 		@Override
 		public List<T> findByCondition(T obj) {
 			if(obj == null) {
 				throw new BusinessException(" condition can't null!");
 			}
 			return (List<T>) sqlSessionTemplate.selectList(obj.getClass().getName()+ PAGESELECT, obj);
 		}

 		@SuppressWarnings("unchecked")
 		@Override
 		public List<T> findByCondition(T obj, int offset, int limit) {
 			if(obj == null) {
 				throw new BusinessException(" condition can't null!");
 			}
 			RowBounds rb = new RowBounds(offset, limit);
 			return (List<T>) sqlSessionTemplate.selectList(obj.getClass().getName()+PAGESELECT, obj, rb);
 		}
 		
 		@Override
 		public List<T> findByCondition(T obj, Pager pager) {
 			if(obj == null) {
 				throw new BusinessException(" condition can't null!");
 			}
 			 if (pager != null) {
 				 return this.findByCondition(obj,pager.getStartRow(),pager.getPageSize()); 
 			 } else {
 				 return this.findByCondition(obj);
 			 }
 		}

 		@SuppressWarnings("unchecked")
 		@Override
 		public T findByPK(PK pk, Class<T> cls) {
 			if(pk == null) {
 				throw new BusinessException(" pk can't null!");
 			}
 			return (T) sqlSessionTemplate.selectOne(cls.getName()+GETBYID, pk);
 	 
 		}

 		@Override
 		public void update(T object) {
 			if(object == null) {
 				throw new BusinessException(" object can't null!");
 			}
 			if(JtaContext.TRANSACTION_LOCAL.get() != null) {
 				SqlSession session = sqlSessionFactory.openSession(false);
 				setAutoCommit(session.getConnection(), false);
 				JtaContext.CONNECTION_HOLDER.get().add(session);
 				 session.update(object.getClass().getName()+ UPDATE,object);
 			} else {
 				 sqlSessionTemplate.update(object.getClass().getName()+UPDATE, object);

 			}
 		}

 		@Override
 		public void delete(PK pk, Class<T> cls) {
 			if(pk == null) {
 				throw new BusinessException(" pk can't null!");
 			}
 			if(JtaContext.TRANSACTION_LOCAL.get() != null) {
 				SqlSession session = sqlSessionFactory.openSession(false);
 				setAutoCommit(session.getConnection(), false);
 				JtaContext.CONNECTION_HOLDER.get().add(session);
 				 session.delete(cls.getName()+DELETE, pk);
 			} else {
 				 sqlSessionTemplate.delete(cls.getName()+DELETE, pk);
 			}
 			
 		}

 		@Override
 		public Integer getTotalCount(T object) {
 			if(object == null) {
 				throw new BusinessException(" condition can't null!");
 			}
 			Object obj = sqlSessionTemplate.selectOne(object.getClass().getName()+COUNT, object);
 			if (obj != null) {
 				return Integer.parseInt(obj.toString());
 			}
 			return 0;
 		}


 		@Override
 		public void insertBatch(Class<T> cls, List<T> domainList) {
 			sqlSessionTemplate.insert(cls.getName()+INSERT_BATCH, domainList);
 		}
 		
 		@Override
 		public void insertBatch(Class<T> cls, List<T> domainList, Integer count) {
 			SqlSession sqlSession = null;
 			try {
 				if (domainList == null) {
 					return;
 				}
 			 
 				sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
 	 
 				if(JtaContext.TRANSACTION_LOCAL.get() != null) {
 					setAutoCommit(sqlSession.getConnection(), false);
 					JtaContext.CONNECTION_HOLDER.get().add(sqlSession);
 	 			}  
 				
 	            int num = 0;
 				for (T t : domainList) {
 					 
 					sqlSession.insert(cls.getName()+ INSERT, t);
 					 num++; 
 					 if (count == num) {
 						 sqlSession.commit();
 						 num = 0;
 					 }
 				}
 	 
 				if(JtaContext.TRANSACTION_LOCAL.get() == null) {
 					sqlSession.commit();
 	 			}  
 				
 				
 				
 			}catch (Exception e) {
 				if(JtaContext.TRANSACTION_LOCAL.get() == null) {
 					if (sqlSession != null) {
 						   sqlSession.rollback(true);
 					}
 				}
 				
 				logger.error(e.getMessage(),e);
 			}  finally {
 				if(JtaContext.TRANSACTION_LOCAL.get() == null) {
 					if (sqlSession != null) {
 						sqlSession.close();
 					}
 				}
 				
 			}
 		}

 		@Override
 		public void updateBatch(Class<T> cls, List<T> domainList, Integer count) {
 			SqlSession sqlSession = null;
 			try {
 				if (domainList == null) {
 					return;
 				}
 				sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
 				if(JtaContext.TRANSACTION_LOCAL.get() != null) {
 					setAutoCommit(sqlSession.getConnection(), false);
 					JtaContext.CONNECTION_HOLDER.get().add(sqlSession);
 	 			}  
 				
 				int num = 0;
 				for (T t : domainList) {
 					 sqlSession.update(cls.getName()+ UPDATE, t);
 					 num++; 
 					 if (count == num) {
 						 sqlSession.commit();
 						 num = 0;
 					 }
 				}
 				if(JtaContext.TRANSACTION_LOCAL.get() == null) {
 					sqlSession.commit();
 				}
 	 			
 			}catch (Exception e) {
 				if(JtaContext.TRANSACTION_LOCAL.get() == null) {
 					if (sqlSession != null) {
 						   sqlSession.rollback(true);
 					}
 				}
 				
 				logger.error(e.getMessage(),e);
 			}  finally {
 				if(JtaContext.TRANSACTION_LOCAL.get() == null) {
 					if (sqlSession != null) {
 						sqlSession.close();
 					}
 				}
 				
 			}
 		}
 		
 		/**
 		 * 获得当前方法对应的语句映射Id,如果提供的id不为空， 则直接返回，否则则按照默认规则生成的id.
 		 * 
 		 * @param statement
 		 * @return
 		 */
 		@SuppressWarnings("unused")
 		private String getStatment(String statement) {
 			if (StringUtils.isEmpty(statement)) {
 				return this.getStatement();
 			}
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
 			StringBuffer bufId = new StringBuffer(invokingMethod.getName());
 			for (Method m : methods) {
 				if (m.getName().equals(invokingMethod.getName())
 						&& !m.equals(invokingMethod)) {
 					Class<?>[] s = m.getParameterTypes();
 					if (s != null && s.length > 0) {
 						for (Class<?> z : s) {
 							bufId.append("_").append(z.getSimpleName());
 						}
 					}
 				}
 			}
 			return namespace + "." + bufId.toString();
 		}
 		

 		public Object getTarget() {
 			return target;
 		}

 		public void setTarget(Object target) {
 			this.target = target;
 		}

 		public Method getInvokingMethod() {
 			return invokingMethod;
 		}

 		public void setInvokingMethod(Method invokingMethod) {
 			this.invokingMethod = invokingMethod;
 		}

 		@Override
 		public SqlSessionTemplate getCurSqlSessionTemplate() {	    
 			return sqlSessionTemplate;
 		}

 		public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
 			this.sqlSessionFactory = sqlSessionFactory;
 		}

 		public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
 			this.sqlSessionTemplate = sqlSessionTemplate;
 		}

 		@Override
 		public SqlSessionFactory getCurSqlSessionFactory() {
 		   return this.sqlSessionFactory;
 		}

 		private void setAutoCommit(Connection con,boolean autoCommit) {
 			 try {
 				 if(con != null) {
 					 con.setAutoCommit(autoCommit);
 				 }
 			 }catch (Exception e) {
 			 
 			}
 		}

 	 

 		@Override
 		public int updateExp(T object) {
 			if(object == null) {
 				throw new BusinessException(" object can't null!");
 			}
 			if(JtaContext.TRANSACTION_LOCAL.get() != null) {
 				SqlSession session = sqlSessionFactory.openSession(false);
 				setAutoCommit(session.getConnection(), false);
 				JtaContext.CONNECTION_HOLDER.get().add(session);
 				return session.update(object.getClass().getName()+ UPDATE,object);
 			} else {
 				return sqlSessionTemplate.update(object.getClass().getName()+UPDATE, object);

 			}
 		}

 		@Override
 		public int deleteExp(PK pk, Class<T> cls) {
 			if(pk == null) {
 				throw new BusinessException(" pk can't null!");
 			}
 			if(JtaContext.TRANSACTION_LOCAL.get() != null) {
 				SqlSession session = sqlSessionFactory.openSession(false);
 				setAutoCommit(session.getConnection(), false);
 				JtaContext.CONNECTION_HOLDER.get().add(session);
 				 return session.delete(cls.getName()+DELETE, pk);
 			} else {
 				return sqlSessionTemplate.delete(cls.getName()+DELETE, pk);
 			}
 		}
 
}
