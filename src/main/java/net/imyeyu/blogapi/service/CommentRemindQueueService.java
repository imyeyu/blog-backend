package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.CommentRemindQueue;

import java.util.List;

/**
 * 评论回复队列服务
 *
 * <p>夜雨 创建于 2021-08-25 00:11
 */
public interface CommentRemindQueueService extends BaseService<CommentRemindQueue> {

	/**
	 * 根据用户 ID 获取
	 *
	 * @param uid 用户 ID
	 * @return 回复提醒列表
	 * @throws ServiceException 服务异常
	 */
	List<CommentRemindQueue> findManyByUID(Long uid) throws ServiceException;

	/**
	 * 根据用户 ID 删除
	 *
	 * @param uid 用户 ID
	 * @throws ServiceException 服务异常
	 */
	void deleteByUID(Long uid) throws ServiceException;

	/**
	 * 根据回复 ID 删除
	 *
	 * @param rid 回复 ID
	 * @throws ServiceException 服务异常
	 */
	void deleteByRID(Long rid) throws ServiceException;
}