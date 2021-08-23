package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.Comment;
import net.imyeyu.blogapi.entity.CommentReply;
import net.imyeyu.blogapi.entity.UserComment;

import java.util.List;

/**
 * 评论服务
 *
 * <p>夜雨 创建于 2021-02-23 21:32
 */
public interface CommentService extends BaseService<Comment> {

	/**
	 * 根据文章 ID 获取评论，每次 10 条，包含子评论（最新的十条）
	 *
	 * @param aid    文章 ID
	 * @param offset 偏移
	 * @return 评论列表
	 */
	List<Comment> findMany(Long aid, Long offset);

	/** @param commentReply 回复评论对象 */
	void createReply(CommentReply commentReply);

	/**
	 * 获取回复评论
	 *
	 * @param crid 回复评论 ID
	 * @return 回复评论
	 */
	CommentReply findReply(Long crid);

	/**
	 * 获取子评论
	 *
	 * @param cid    父级评论
	 * @param offset 查询偏移量
	 * @return 回复列表
	 */
	List<CommentReply> findManyReplies(Long cid, Long offset);

	/**
	 * 根据用户 ID 获取相关评论，包括评论、回复和被回复数据
	 *
	 * @param uid    用户 ID
	 * @param offset 偏移
	 * @param limit  数量
	 * @return 用户评论列表
	 */
	List<UserComment> findManyUserComment(Long uid, Long offset, int limit);

	/**
	 * 根据用户 ID 获取相关回复数据
	 *
	 *
	 * @param uid    用户 ID
	 * @param offset 偏移
	 * @param limit  数量
	 * @return 用户评论列表
	 */
	List<UserComment> findManyUserCommentReplies(Long uid, Long offset, int limit);

	/**
	 * 统计该文章的评论数量
	 *
	 * @param aid 文章 ID
	 * @return 评论数量
	 */
	int getLength(Long aid);

	/**
	 * 删除用户评论，包括关联回复（账号注销调用）
	 *
	 * @param uid 用户 ID
	 * @throws ServiceException 服务异常
	 */
	void deleteByUID(Long uid) throws ServiceException;

	/**
	 * 删除指定回复（用户调用、账号注销调用）
	 *
	 * @param id 索引
	 * @throws ServiceException 服务异常
	 */
	void deleteReply(Long id) throws ServiceException;

	/**
	 * 删除该用户所有回复和被回复（账号注销调用）
	 *
	 * @param uid 用户 ID
	 * @throws ServiceException 服务异常
	 */
	void deleteReplyByUID(Long uid) throws ServiceException;
}