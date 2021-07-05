package net.imyeyu.blog.service;

import net.imyeyu.blog.entity.ArticleLabel;

/**
 * 文章标签服务
 *
 * <p>夜雨 创建于 2021-07-04 19:33
 */
public interface ArticleLabelService extends BaseService<ArticleLabel>{

	ArticleLabel findByName(String name);
}
