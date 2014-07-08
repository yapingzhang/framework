package cn.bidlink.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bidlink.demo.dao.UserDao;
import cn.bidlink.demo.model.User;
import cn.bidlink.demo.service.UserService;

@Service
@Transactional(readOnly = false)
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserDao userDao;
	
	@Transactional(readOnly = true)
	public List<User> findAll(){
		return this.userDao.findAll();
	}

	@Override
	public void save(User user) {
		this.userDao.save(user);
	}

	@Override
	public void update(User user) {
		this.userDao.update(user);
	}

	@Override
	public void delete(Long id) {
		this.userDao.delete(id, User.class);
	}

	@Override
	@Transactional(readOnly = true)
	public User findByid(Long id) {
		return this.userDao.findByPK(id, User.class);
	}
}
