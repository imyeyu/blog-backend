package net.imyeyu.blogapi.vo;

import lombok.Data;
import net.imyeyu.blogapi.entity.ArticleClass;

import java.util.List;

/**
 * 文章分类（侧边栏）
 *
 * 夜雨 创建于 2021-07-12 22:01
 */
@Data
public class ArticleClassSide {

	private List<ArticleClass> main;
	private List<ArticleClass> other;
}