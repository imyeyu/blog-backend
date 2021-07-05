package net.imyeyu.blog.mapper;

import net.imyeyu.blog.entity.Article;
import net.imyeyu.blog.service.BaseService;

import java.util.List;

/**
 * 文章
 *
 * 夜雨 创建于 2021/2/23 21:34
 */
public interface ArticleMapper extends BaseService<Article> {

	List<Article> findManyByList(long offset, int limit);

	void clearLabels(long aid);

	void addLabel(long aid, long lid);
}