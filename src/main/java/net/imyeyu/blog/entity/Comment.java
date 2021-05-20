package net.imyeyu.blog.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 评论
 *
 * 夜雨 创建于 2021-02-25 14:46
 */
@Data
public class Comment implements Serializable {

	private Long id;

	private Long articleId;
	private Long userId;
	private String nick;
	private String data;

	private Long createdAt;
	private Long updatedAt;
	private Long deletedAt;

	private int repliesLength;
	private User user;
	private List<CommentReply> replies;
}