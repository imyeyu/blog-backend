package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.User;
import net.imyeyu.blogapi.entity.UserData;
import net.imyeyu.blogapi.vo.UserSignedIn;

/**
 * 用户管理服务
 * 
 * <p>夜雨 创建于 2021-02-23 21:32
 */
public interface UserService extends BaseService<User> {

	/**
	 * 注册用户，传入参 User.password 是明文
	 *
	 * @param user 用户数据
	 * @return 自动登录数据
	 * @throws ServiceException 服务异常
	 */
	UserSignedIn register(User user) throws ServiceException;

	/**
	 * 执行登录
	 *
	 * @param user     uid、邮件或用户名
	 * @param password 明文密码
	 * @return 该账号数据
	 * @throws ServiceException 服务异常
	 */
	UserSignedIn signIn(String user, String password) throws ServiceException;

	/**
	 * 校验该令牌是否已登录（令牌有 UID，不需要传）
	 *
	 * @param token 令牌
	 * @return true 为已登录
	 * @throws ServiceException 服务异常
	 */
	boolean isSignedIn(String token) throws ServiceException;

	/**
	 * 退出登录
	 *
	 * @param token 令牌
	 * @return true 为退出成功
	 * @throws ServiceException 服务异常
	 */
	boolean signOut(String token) throws ServiceException;

	/**
	 * 根据用户名查找
	 *
	 * @param name 用户名
	 * @return 账号数据
	 */
	User findByName(String name);

	/**
	 * 根据邮箱查找
	 *
	 * @param email 邮箱
	 * @return 账号数据
	 */
	User findByEmail(String email);

	/**
	 * 创建用户资料
	 *
	 * @param userData 数据体
	 */
	void createData(UserData userData);

	/**
	 * 查找用户资料
	 *
	 * @param userId uid
	 * @return 用户资料
	 * @throws ServiceException 服务异常
	 */
	UserData findData(Long userId) throws ServiceException;

	/**
	 * 更新用户资料
	 *
	 * @param userData 数据体
	 */
	void updateData(UserData userData);

	/**
	 * 删除用户资料
	 *
	 * @param userId uid
	 */
	void deleteData(Long userId);
}