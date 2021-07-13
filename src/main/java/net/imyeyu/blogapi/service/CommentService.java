package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.entity.Comment;
import net.imyeyu.blogapi.entity.CommentReply;

import java.util.List;

/**
 * 评论服务
 *
 * <p>夜雨 创建于 2021-02-23 21:32
 */
public interface CommentService extends BaseService<Comment> {

	/**
	 * 根据文章 ID 获取评论，每次 10 条，包含子评论（最早的十条）
	 * 
	 * @param articleId 文章 ID
	 * @param offset    查询偏移量
	 * @return 评论列表
	 */
	List<Comment> findByArticleId(Long articleId, Long offset);
	
	/**
	 * 获取子评论
	 * 
	 * @param commentId 父级评论
	 * @param offset    查询偏移量
	 * @return 回复列表
	 */
	List<CommentReply> findRepliesByCommentId(Long commentId, Long offset);
	
	/** @param commentReply 回复评论对象 */
	void createReply(CommentReply commentReply);
}