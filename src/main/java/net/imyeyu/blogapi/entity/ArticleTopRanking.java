package net.imyeyu.blogapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 访问排行（每周）
 * 只记录访问次数、标题和最近访问，具体文章有 Redis key 记录
 *
 * <p>夜雨 创建于 2021-03-01 17:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleTopRanking extends BaseEntity implements Serializable {

	private String title;
	private ArticleType type;
	private int count = 1;
	private Long recentAt; // 最近访问

	public ArticleTopRanking(Long id, String title, ArticleType type) {
		setId(id);
		this.title = title;
		this.type = type;
	}

	/** 访问计数 + 1 */
	public void increment() {
		count++;
	}
}