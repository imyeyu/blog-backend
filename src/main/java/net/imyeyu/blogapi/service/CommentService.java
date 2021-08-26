package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.Comment;

import java.util.List;

/**
 * 评论服务
 *
 * <p>夜雨 创建于 2021-02-23 21:32
 */
public interface CommentService extends BaseService<Comment> {

	/**
	 * 根据文章 ID 获取评论，包含子评论（最新的六条）
	 *
	 * @param aid    文章 ID
	 * @param offset 偏移
	 * @param limit  数量
	 * @return 评论列表
	 */
	List<Comment> findMany(Long aid, Long offset, int limit) throws ServiceException;

	/**
	 * 删除用户评论，包括关联回复（账号注销调用）
	 *
	 * @param uid 用户 ID
	 * @throws ServiceException 服务异常
	 */
	void deleteByUID(Long uid) throws ServiceException;
}