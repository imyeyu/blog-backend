package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.Article;

import java.util.List;

/**
 * 文章
 *
 * <p>夜雨 创建于 2021-02-23 21:34
 */
public interface ArticleMapper extends BaseMapper<Article> {

	List<Article> findManyByClass(Long cid, Long offset, int limit);

	List<Article> findManyByLabel(Long lid, Long offset, int limit);
}