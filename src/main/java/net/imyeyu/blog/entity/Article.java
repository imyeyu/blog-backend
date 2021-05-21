package net.imyeyu.blog.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 文章
 *
 * 夜雨 创建于 2021/3/1 17:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Article extends BaseEntity implements Serializable {

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

	/** 评论一次 */
	public void comment() {
		comments++;
	}

	/** 阅读一次 */
	public void read() {
		reads++;
	}
}