package cn.bidlink.demo.dao;

import java.util.List;

import cn.bidlink.demo.model.User;
import cn.bidlink.framework.dao.ibatis.MyBatisBaseDao;

public interface UserDao extends MyBatisBaseDao<User, java.lang.Long>{
	
	/**
	 * 查询所有用户。
	 * @return
	 */
	public List<User> findAll();


}
