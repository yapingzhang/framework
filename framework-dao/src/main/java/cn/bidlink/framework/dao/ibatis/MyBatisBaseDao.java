package cn.bidlink.framework.dao.ibatis;
import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

import cn.bidlink.framework.core.utils.pager.Pager;
/**
 * MyBatis DAO通用操作类
 * @author LDJ
 * @param <T>
 * @param <PK>
 * @Date 2007-07-02
 */
public interface MyBatisBaseDao<T, PK extends Serializable>{
	/**
	 * 增加实体
	 * 
	 * @param object
	 */
	public int save(T object);
	
 

	/**
	 * 按条件查询实体
	 * 
	 * @param obj
	 * @return
	 */
	public List<T> findByCondition(T obj);

	/**
	 * 按条件查询实体并分页
	 * 
	 * @param obj
	 * @return
	 */
	public List<T> findByCondition(T obj, int start, int limit);
	
	/**
	 * 
	 * @description 分页查询
	 * @param   obj 查询对象
	 * @param   pager 分页对象
	 * @return  查询结果
	 * @throws
	 */
	public List<T> findByCondition(T obj,Pager pager);

	/**
	 * 按主键查询
	 * 
	 * @param pk
	 * @return
	 */
	public T findByPK(PK pk, Class<T> cls);

	/**
	 * 更新实体
	 * 
	 * @param object
	 */
	public void update(T object);

	public int updateExp(T object);
	/**
	 * 按主键删除实体
	 * 
	 * @param pk
	 */
	public void delete(PK pk, Class<T> cls);
	
	public int deleteExp(PK pk, Class<T> cls);

	/**
	 * 按条件查询总记录数
	 * 
	 * @param object
	 * @return
	 */
	public Integer getTotalCount(T object);
	
	/**
	 * 批量插入
	 * @param statementname 更新SQL的ID（sqlMap中）
	 * @param domainList 需要更新的集合
	 * @param count 表示多少笔数据提交一次
	 */
	public void insertBatch(Class<T> cls, List<T> domainList,Integer count);
	
	/**
	 * 批量插入
	 * @description 基本原生SQL的数据库插入
	 * @param cls
	 * @param domainList 
	 * @throws
	 */
	public void insertBatch(Class<T> cls, List<T> domainList);
	
	/**
	 * 批量更新
	 * @param statementname 更新SQL的ID（sqlMap中）
	 * @param domainList 需要更新的集合
	 * @param count 表示多少笔数据提交一次
	 */
	public void updateBatch(Class<T> cls, List<T> domainList,Integer count);
	
 
	/**
	 * 取得当前SqlManClient
	* @Title: getCurSqlSessionTemplate
	* @Description: TODO描述
	* @param @return    设定文件
	* @return SqlSessionTemplate    返回类型
	* @throws
	 */
	public SqlSessionTemplate getCurSqlSessionTemplate();
	
	public SqlSessionFactory getCurSqlSessionFactory();
	
}

