package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.Comment;
import net.imyeyu.blogapi.entity.CommentReply;

import java.util.List;

/**
 * 评论
 *
 * <p>夜雨 创建于 2021/2/23 21:33
 */
public interface CommentMapper extends BaseMapper<Comment> {

	/**
	 * 统计文章评论和回复
	 *
	 * @param aid 文章 ID
	 * @return 数量
	 */
	int getLength(Long aid);

	List<Comment> findMany(Long articleId, Long offset);

	List<Comment> findAllByUID(Long uid);

	void createReply(CommentReply commentReply);

	List<CommentReply> findManyReplies(Long commentId, Long offset);

	List<CommentReply> findAllRepliesByCID(Long cid);

	void deleteByUID(Long uid);

	void deleteReply(Long id);

	void deleteReplyByUID(Long uid);
}