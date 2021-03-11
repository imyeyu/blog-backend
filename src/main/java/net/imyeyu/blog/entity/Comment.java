package net.imyeyu.blog.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 评论
 *
 * 夜雨 创建于 2021/2/25 14:46
 */
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Long deletedAt) {
		this.deletedAt = deletedAt;
	}

	public int getRepliesLength() {
		return repliesLength;
	}

	public void setRepliesLength(int repliesLength) {
		this.repliesLength = repliesLength;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<CommentReply> getReplies() {
		return replies;
	}

	public void setReplies(List<CommentReply> replies) {
		this.replies = replies;
	}
}