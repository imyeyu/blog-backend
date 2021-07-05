package net.imyeyu.blog.service;

import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.Article;
import net.imyeyu.blog.entity.ArticleHot;
import net.imyeyu.blog.entity.ArticleLabel;

import java.util.List;

/**
 * 文章服务
 *
 * 夜雨 创建于 2021/2/23 21:33
 */
public interface ArticleService extends BaseService<Article> {

	/**
	 * 查询文章（主页列表，过滤隐藏的文章）
	 *
	 * @param offset 偏移
	 * @param limit  数量
	 * @return 文章列表
	 */
	List<Article> findManyByList(long offset, int limit);

	/** @return 每周阅读排行 */
	List<ArticleHot> getArticleHot() throws ServiceException;

	/** 评论计数 */
	void comment(Article article);

	/** 更新阅读计数，包括触发每周热门排行，同一 IP 3 小时内访问多次的文章只计一次 */
	void read(String ip, Article article);

	/**
	 * 同步标签
	 *
	 * @param article 文章
	 */
	void syncLabels(Article article);
}