package net.imyeyu.blogapi.entity;

import lombok.Data;

/**
 * 评论回复记录（个人空间"回复我的"）
 *
 * <p>夜雨 创建于 2021-08-25 14:07
 */
@Data
public class CommentReplyRecord {

	private Long userId;
	private Long replyId;
	private Long createdAt;

	private CommentReply reply;
}