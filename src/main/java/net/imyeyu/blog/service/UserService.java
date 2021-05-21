package net.imyeyu.blog.service;

import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.User;

/**
 * 用户管理接口
 * 
 * 夜雨 创建于 2021-02-23 21:32
 */
public interface UserService extends BaseService<User> {

	/**
	 * 执行登录
	 *
	 * @param user     UID、邮件或用户名
	 * @param password 密码
	 * @return true 为登录成功
	 */
	boolean doSignin(String user, String password) throws ServiceException;

	User findByName(String name);

	User findByEmail(String email);
}