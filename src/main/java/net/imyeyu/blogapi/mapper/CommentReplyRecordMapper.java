package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.CommentReplyRecord;
import net.imyeyu.blogapi.entity.UserComment;

import java.util.List;

/**
 * <p>夜雨 创建于 2021-08-25 14:39
 */
public interface CommentReplyRecordMapper extends BaseMapper<CommentReplyRecord> {

	List<UserComment> findManyByUID(Long uid, Long offset, int limit);

	void deleteByRID(Long rid);

	void deleteByRIDwithUID(Long uid, Long rid);
}
