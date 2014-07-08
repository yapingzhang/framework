package cn.bidlink.demo.dao.impl;

import java.util.List;

import cn.bidlink.demo.dao.UserDao;
import cn.bidlink.demo.model.User;
import cn.bidlink.framework.core.annotation.Dao;
import cn.bidlink.framework.dao.ibatis.impl.MyBatisBaseDaoImpl;

@Dao
public class UserDaoImpl extends MyBatisBaseDaoImpl<User,java.lang.Long> implements UserDao{

	@Override
	public List<User> findAll() {
		return this.getCurSqlSessionTemplate().selectList("findAll");
	}
	
	

}
