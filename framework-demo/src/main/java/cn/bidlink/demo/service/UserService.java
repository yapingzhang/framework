package cn.bidlink.demo.service;

import java.util.List;

import cn.bidlink.demo.model.User;

public interface UserService {
	
	/**
	 * 根据主键查询用户ID。
	 * @return
	 */
	public User findByid(Long id);
	
	/**
	 * 查询所有用户。
	 * @return
	 */
	public List<User> findAll();
	
	/**
	 * 保存新的用户。
	 * @param user
	 */
	public void save(User user);

	/**
	 * 更新旧的用户。
	 * @param user
	 */
	public void update(User user);
	
	/**
	 * 删除用户。
	 * @param user
	 */
	public void delete(Long id);

}
