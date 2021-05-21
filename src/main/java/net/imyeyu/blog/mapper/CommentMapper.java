package net.imyeyu.blog.mapper;

import java.util.List;

import net.imyeyu.blog.entity.Comment;
import net.imyeyu.blog.entity.CommentReply;
import org.apache.ibatis.annotations.Param;

/**
 * 评论 SQL 映射
 *
 * 夜雨 创建于 2021/2/23 21:33
 */
public interface CommentMapper {

	List<Comment> findByArticleId(@Param("articleId") Long articleId, @Param("offset") Long offset);
	
	List<CommentReply> findRepliesByCommentId(@Param("commentId") Long commentId, @Param("offset") Long offset);

	Long create(@Param("comment") Comment comment);
	
	void createReply(@Param("commentReply") CommentReply commentReply);
}
