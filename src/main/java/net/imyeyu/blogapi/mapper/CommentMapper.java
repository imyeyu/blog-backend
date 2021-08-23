package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.Comment;
import net.imyeyu.blogapi.entity.CommentReply;
import net.imyeyu.blogapi.entity.UserComment;

import java.util.List;

/**
 * 评论
 *
 * <p>夜雨 创建于 2021/2/23 21:33
 */
public interface CommentMapper extends BaseMapper<Comment> {

	List<Comment> findMany(Long aid, Long offset);

	List<Comment> findAllByUID(Long uid);

	void createReply(CommentReply commentReply);

	// --

	CommentReply findReply(Long crid);

	List<CommentReply> findManyReplies(Long cid, Long offset);

	List<CommentReply> findAllRepliesByCID(Long cid);

	// --

	List<UserComment> findManyUserComment(Long uid, Long offset, int limit);

	List<UserComment> findManyUserCommentReplies(Long uid, Long offset, int limit);

	// --

	int getLength(Long aid);

	// --

	boolean deleteByUID(Long uid);

	boolean deleteReply(Long id);

	boolean deleteReplyByUID(Long uid);
}