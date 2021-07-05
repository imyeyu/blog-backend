package net.imyeyu.blog.service;

import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.User;
import net.imyeyu.blog.entity.UserData;
import net.imyeyu.blog.vo.UserSignedIn;

/**
 * 用户管理接口
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
	 * 校验该 ID 是否已登录
	 *
	 * @param uid   uid
	 * @param token 令牌
	 * @return true 为已登录
	 * @throws ServiceException 服务异常
	 */
	boolean isSignedIn(Long uid, String token) throws ServiceException;

	/**
	 * 退出登录
	 *
	 * @param uid   uid
	 * @param token 令牌
	 * @return true 为退出成功
	 * @throws ServiceException 服务异常
	 */
	boolean signOut(Long uid, String token) throws ServiceException;

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