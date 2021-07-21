package net.imyeyu.blogapi.entity;

import com.google.gson.JsonObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 文章
 *
 * <p>夜雨 创建于 2021-03-01 17:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Article extends BaseEntity implements Serializable {

	private String title;
	private ArticleType type;
	private ArticleClass clazz;
	private List<ArticleLabel> labels;
	private String digest;
	private String data;
	private JsonObject extendData;

	private int reads;
	private int likes;
	private int comments; // 评论数量，仅在服务调用 find() 时有数据
	private boolean canComment;
	private boolean canRanking;

	/** 阅读一次 */
	public void read() {
		reads++;
	}

	/** 喜欢一次 */
	public void like() { likes++; }
}