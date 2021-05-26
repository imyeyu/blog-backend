package net.imyeyu.blog.service;

import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.User;
import net.imyeyu.blog.vo.UserVO;

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
	 * @return 该账号数据
	 * @throws ServiceException 校验异常
	 */
	UserVO doSignIn(String user, String password) throws ServiceException;

	/**
	 * 校验该 ID 是否已登录
	 *
	 * @param uid   ID
	 * @param token 令牌
	 * @return 该账号数据
	 * @throws ServiceException 校验异常
	 */
	boolean isSignedIn(Long uid, String token) throws ServiceException;

	User findByName(String name);

	User findByEmail(String email);
}