package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.ArticleLabel;
import net.imyeyu.blogapi.service.BaseService;

import java.util.List;

/**
 * 文章标签
 *
 * <p>夜雨 创建于 2021-07-04 19:35
 */
public interface ArticleLabelMapper extends BaseService<ArticleLabel> {

	ArticleLabel findByName(String name);

	List<ArticleLabel> findManyByArticleId(long aid);

	void clearLabels(long aid);

	void addLabel(long aid, long lid);
}
