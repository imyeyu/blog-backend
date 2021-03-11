package net.imyeyu.blog.entity;

import java.io.Serializable;

/**
 * 访问排行（每周）
 * 只记录访问次数、标题和最近访问，具体文章有 Redis key 记录
 *
 * 夜雨 创建于 2021/3/1 17:10
 */
public class ArticleHot implements Serializable {

	private long id;
	private String title;
	private int count = 1;
	private Long recentAt; // 最近访问

	public ArticleHot(long id, String title) {
		this.id = id;
		this.title = title;
	}

	public void increment() {
		count++;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Long getRecentAt() {
		return recentAt;
	}

	public void setRecentAt(Long recentAt) {
		this.recentAt = recentAt;
	}
}