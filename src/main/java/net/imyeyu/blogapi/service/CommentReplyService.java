package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.CommentReply;

import java.util.List;

/**
 * 评论回复服务
 *
 * <p>夜雨 创建于 2021-08-24 10:33
 */
public interface CommentReplyService extends BaseService<CommentReply> {

	/**
	 * 查询部分回复
	 *
	 * @param cid 评论 ID
	 * @param offset    偏移
	 * @param limit     数量
	 * @return 回复列表
	 * @throws ServiceException 服务异常
	 */
	List<CommentReply> findMany(Long cid, Long offset, int limit) throws ServiceException;

	/**
	 * 删除该用户所有回复和被回复（账号注销调用）
	 *
	 * @param uid 用户 ID
	 * @throws ServiceException 服务异常
	 */
	void deleteByUID(Long uid) throws ServiceException;

	/**
	 * 删除该评论的所有回复和被回复
	 *
	 * @param cid 评论 ID
	 * @throws ServiceException 服务异常
	 */
	void deleteByCID(Long cid) throws ServiceException;
}