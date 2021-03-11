package net.imyeyu.blog.entity;

import java.io.Serializable;

/**
 * 文章
 *
 * 夜雨 创建于 2021/3/1 17:10
 */
public class Article implements Serializable {

	private Long id;
	private String title;
	private String type;
	private String clazz;
	private String label;
	private String digest;
	private String data;

	private int reads;
	private int likes;
	private int comments;

	private boolean isHide;

	private Long createdAt;
	private Long updatedAt;
	private Long deletedAt;

	/** 评论一次 */
	public void comment() {
		comments++;
	}

	/** 阅读一次 */
	public void read() {
		reads++;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getReads() {
		return reads;
	}

	public void setReads(int reads) {
		this.reads = reads;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public boolean isHide() {
		return isHide;
	}

	public void setHide(boolean hide) {
		isHide = hide;
	}

	public boolean getIsHide() {
		return isHide;
	}

	public void setIsHide(boolean hide) {
		isHide = hide;
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
}