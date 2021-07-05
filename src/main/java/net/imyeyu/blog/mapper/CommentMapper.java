package net.imyeyu.blog.mapper;

import java.util.List;

import net.imyeyu.blog.entity.Comment;
import net.imyeyu.blog.entity.CommentReply;
import net.imyeyu.blog.service.BaseService;
import org.apache.ibatis.annotations.Param;

/**
 * 评论
 *
 * 夜雨 创建于 2021/2/23 21:33
 */
public interface CommentMapper extends BaseService<Comment> {

	List<Comment> findByArticleId(@Param("articleId") Long articleId, @Param("offset") Long offset);
	
	List<CommentReply> findRepliesByCommentId(@Param("commentId") Long commentId, @Param("offset") Long offset);

	void createReply(@Param("data") CommentReply commentReply);
}
