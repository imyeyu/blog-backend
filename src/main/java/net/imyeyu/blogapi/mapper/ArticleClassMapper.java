package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.ArticleClass;

import java.util.List;

/**
 * 文章分类
 *
 * <p>夜雨 创建于 2021-07-04 19:30
 */
public interface ArticleClassMapper extends BaseMapper<ArticleClass> {

	List<ArticleClass> findMain();

	List<ArticleClass> findOther();
}