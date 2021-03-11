package net.imyeyu.blog.service;

import net.imyeyu.blog.entity.Article;
import net.imyeyu.blog.entity.ArticleHot;

import java.util.List;

/**
 * 文章服务
 *
 * 夜雨 创建于 2021/2/23 21:33
 */
public interface ArticleService extends BaseService<Article> {

	/** @return 每周阅读排行 */
	List<ArticleHot> getArticleHot();

	/** 评论计数 */
	void comment(Article article);

	/** 更新阅读计数，包括触发每周热门排行，同一 IP 3 小时内访问多次的文章只计一次 */
	void read(String ip, Article article);
}