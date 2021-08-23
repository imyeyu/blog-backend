package net.imyeyu.blogapi.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户评论，由用户个人空间调用（我的评论和回复我的）
 *
 * <p>夜雨 创建于 2021-08-23 15:45
 */
@Data
public class UserComment implements Serializable {
	// 文章
	private Long aid;
	private String aTitle;
	private ArticleType aType;
	// 评论
	private Long cid;
	private Long crid;
	private String data;
	private Long createdAt;
	// 用户
	private Long uid;
	private String name;
	private Boolean hasAvatar;
}