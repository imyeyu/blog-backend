package net.imyeyu.blogapi.mapper;

import java.util.List;

import net.imyeyu.blogapi.entity.Comment;
import net.imyeyu.blogapi.entity.CommentReply;
import net.imyeyu.blogapi.service.BaseService;
import org.apache.ibatis.annotations.Param;

/**
 * 评论
 *
 * <p>夜雨 创建于 2021/2/23 21:33
 */
public interface CommentMapper extends BaseService<Comment> {

	List<Comment> findByArticleId(@Param("articleId") Long articleId, @Param("offset") Long offset);
	
	List<CommentReply> findRepliesByCommentId(@Param("commentId") Long commentId, @Param("offset") Long offset);

	void createReply(@Param("data") CommentReply commentReply);
}
