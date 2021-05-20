package net.imyeyu.blog.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 评论回复
 *
 * 夜雨 创建于 2021-03-01 17:11
 */
@Data
public class CommentReply implements Serializable {

	private Long id;

	private Long commentId;
	private Long senderId;
	private Long receiverId;
	private String senderNick;
	private String receiverNick;
	private String data;

	private Long createdAt;
	private Long updatedAt;
	private Long deletedAt;

	private User sender, receiver;
}