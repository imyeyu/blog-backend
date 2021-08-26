package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.User;
import net.imyeyu.blogapi.entity.UserComment;

import java.util.List;

/**
 * 用户管理服务
 * <p>操作任何用户数据前应保证调用 find，以确保用户存在且没有注销（不需要判断，不存在时 find 会抛异常），如果不需要返回数据，可以调用
 * 一次 exist()，也不需要判断
 * <p>经过令牌验证的操作也不需要检验用户是否存在，不存在的用户无法登录也无法生成正确的令牌
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
	String register(User user) throws ServiceException;

	/**
	 * 执行登录
	 *
	 * @param user     uid、邮件或用户名
	 * @param password 明文密码
	 * @return 通信令牌
	 * @throws ServiceException 服务异常
	 */
	String signIn(String user, String password) throws ServiceException;

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
	 * 修改密码
	 *
	 * @param id          用户 ID
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @return true 为修改成功
	 * @throws ServiceException 服务异常
	 */
	boolean updatePassword(Long id, String oldPassword, String newPassword) throws ServiceException;

	/**
	 * 根据用户名查找
	 *
	 * @param name 用户名
	 * @return 账号数据
	 * @throws ServiceException 服务异常
	 */
	User findByName(String name) throws ServiceException;

	/**
	 * 根据邮箱查找
	 *
	 * @param email 邮箱
	 * @return 账号数据
	 * @throws ServiceException 服务异常
	 */
	User findByEmail(String email) throws ServiceException;

	/**
	 * 用户是否存在，不需要判断，如果不存在直接抛异常。实际上是 find 方法别名而不返回数据
	 *
	 * @param id 用户 ID
	 * @throws ServiceException 服务异常
	 */
	void exist(Long id) throws ServiceException;

	/**
	 * 注销
	 *
	 * @param id       用户 ID
	 * @param password 密码校验
	 * @return true 为注销成功
	 * @throws ServiceException 服务异常
	 */
	boolean cancel(Long id, String password) throws ServiceException;

	/**
	 * 根据用户 ID 获取评论和回复数据（被回复记录独立在 CommentReplyRecordService）
	 *
	 * @param uid    用户 ID
	 * @param offset 偏移
	 * @param limit  数量
	 * @return 用户评论列表
	 */
	List<UserComment> findManyUserComment(Long uid, Long offset, int limit);
}