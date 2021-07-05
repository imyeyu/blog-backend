package net.imyeyu.blog.entity;

import java.io.Serializable;

/**
 * 文章渲染类型，对应前端模板
 *
 * 夜雨 创建于 2021-07-04 09:23
 */
public enum ArticleType implements Serializable {

	PUBLIC,  // 公版
	MUSIC,   // 音乐
	SOFTWARE // 软件
}