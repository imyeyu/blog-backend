package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.entity.ArticleClass;
import net.imyeyu.blogapi.vo.ArticleClassSide;

/**
 * 文章分类服务
 *
 * <p>夜雨 创建于 2021-07-04 19:29
 */
public interface ArticleClassService extends BaseService<ArticleClass>{

	ArticleClassSide findBySide();
}