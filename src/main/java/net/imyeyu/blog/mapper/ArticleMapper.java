package net.imyeyu.blog.mapper;

import net.imyeyu.blog.entity.Article;

import java.util.List;

/**
 * 文章 SQL 映射
 *
 * 夜雨 创建于 2021/2/23 21:34
 */
public interface ArticleMapper {

	List<Article> find(long offset, int limit);

	Article findById(long id);

	void update(Article article);
}