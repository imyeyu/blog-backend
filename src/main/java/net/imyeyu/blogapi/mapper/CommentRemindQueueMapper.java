package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.CommentRemindQueue;

import java.util.List;

/**
 * 评论回复提醒队列
 *
 * <p>夜雨 创建于 2021-08-25 00:15
 */
public interface CommentRemindQueueMapper extends BaseMapper<CommentRemindQueue> {

	List<CommentRemindQueue> findManyByUID(Long uid);

	void deleteByUID(Long uid);

	void deleteByRID(Long uid);
}