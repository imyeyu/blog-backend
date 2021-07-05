package net.imyeyu.blog.mapper;

import net.imyeyu.blog.entity.ArticleLabel;
import net.imyeyu.blog.service.BaseService;

/**
 * 文章标签
 *
 * <p>夜雨 创建于 2021-07-04 19:35
 */
public interface ArticleLabelMapper extends BaseService<ArticleLabel> {

	ArticleLabel findByName(String name);
}
