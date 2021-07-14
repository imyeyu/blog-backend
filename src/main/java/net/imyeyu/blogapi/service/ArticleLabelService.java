package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.entity.ArticleLabel;

import java.util.List;

/**
 * 文章标签服务
 *
 * <p>夜雨 创建于 2021-07-04 19:33
 */
public interface ArticleLabelService extends BaseService<ArticleLabel>{

	/**
	 * 根据标签名查找
	 *
	 * @param name 标签名
	 * @return 文章标签
	 */
	ArticleLabel findByName(String name);

	/**
	 * 查找该文章的标签
	 *
	 * @param aid 文章 ID
	 * @return 标签列表
	 */
	List<ArticleLabel> findManyByArticleId(long aid);
}
