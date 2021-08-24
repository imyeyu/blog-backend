package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.CommentReply;

import java.util.List;

/**
 * <p>夜雨 创建于 2021-08-24 10:36
 */
public interface CommentReplyMapper extends BaseMapper<CommentReply> {

	List<CommentReply> findMany(Long commentId, Long offset, int limit);

	void deleteByUID(Long uid);

	void deleteByCID(Long cid);

	void deleteBySenderID(Long senderId);
}