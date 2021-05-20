package net.imyeyu.blog.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 文章
 *
 * 夜雨 创建于 2021/3/1 17:10
 */
@Data
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
}