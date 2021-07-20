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
public interface CommentMapper extends BaseMapper<Comment> {

	List<Comment> findMany(Long articleId, Long offset);
	
	List<CommentReply> findManyReplies(Long commentId, Long offset);

	int getLength(Long articleId);

	void createReply(CommentReply commentReply);
}
