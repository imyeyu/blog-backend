package net.imyeyu.blog.entity;

import java.io.Serializable;

/**
 * 用户
 *
 * 夜雨 创建于 2021/3/1 17:11
 */
public class User implements Serializable {

	private Long id;
	private String email;
	private String userName;
	private String password;
	private Long createdAt;
	private Long canceledAt;
	private Long deletedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Long getCanceledAt() {
		return canceledAt;
	}

	public void setCanceledAt(Long canceledAt) {
		this.canceledAt = canceledAt;
	}

	public Long getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Long deletedAt) {
		this.deletedAt = deletedAt;
	}
}