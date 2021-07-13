package net.imyeyu.blogapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 评论
 *
 * <p>夜雨 创建于 2021-02-25 14:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Comment extends BaseEntity implements Serializable {

	private Long articleId;
	private Long userId;
	private String nick;
	private String data;

	private int repliesLength;
	private User user;
	private List<CommentReply> replies;
}