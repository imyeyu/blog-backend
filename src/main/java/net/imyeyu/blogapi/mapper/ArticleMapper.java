package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.Article;
import net.imyeyu.blogapi.service.BaseService;

import java.util.List;

/**
 * 文章
 *
 * <p>夜雨 创建于 2021-02-23 21:34
 */
public interface ArticleMapper extends BaseService<Article> {

	List<Article> findManyByList(long offset, int limit);
}