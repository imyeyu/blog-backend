package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.Comment;

import java.util.List;

/**
 * 评论
 *
 * <p>夜雨 创建于 2021/2/23 21:33
 */
public interface CommentMapper extends BaseMapper<Comment> {

	List<Comment> findMany(Long aid, Long offset, int limit);

	List<Comment> findAllByUID(Long uid);

	void deleteByUID(Long uid);
}