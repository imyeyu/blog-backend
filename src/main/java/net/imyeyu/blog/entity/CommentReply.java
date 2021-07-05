package net.imyeyu.blog.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 评论回复
 *
 * <p>夜雨 创建于 2021-03-01 17:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommentReply extends BaseEntity implements Serializable {

	private Long commentId;
	private Long senderId;
	private Long receiverId;
	private String senderNick;
	private String receiverNick;
	private String data;

	private User sender, receiver;
}