package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.ArticleLabel;

import java.util.List;

/**
 * 文章标签
 *
 * <p>夜雨 创建于 2021-07-04 19:35
 */
public interface ArticleLabelMapper extends BaseMapper<ArticleLabel> {

	ArticleLabel findByName(String name);

	List<ArticleLabel> findManyByArticleId(long aid);
}