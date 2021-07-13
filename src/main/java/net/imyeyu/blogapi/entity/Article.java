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
	private int comments;
	private boolean canComment;

	/** 评论一次 */
	public void comment() {
		comments++;
	}

	/** 阅读一次 */
	public void read() {
		reads++;
	}
}