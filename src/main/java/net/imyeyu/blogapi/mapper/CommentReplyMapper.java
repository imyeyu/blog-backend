package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.CommentReply;

import java.util.List;

/**
 * 评论回复
 *
 * <p>夜雨 创建于 2021-08-24 10:36
 */
public interface CommentReplyMapper extends BaseMapper<CommentReply> {

	List<CommentReply> findMany(Long commentId, Long offset, int limit);

	List<CommentReply> findAllByUID(Long uid);

	List<CommentReply> findAllByCID(Long cid);

	void deleteByUID(Long uid);

	void deleteByCID(Long cid);

	void deleteBySenderID(Long senderId);
}