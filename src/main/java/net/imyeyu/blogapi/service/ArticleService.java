package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.Article;
import net.imyeyu.blogapi.entity.ArticleHot;

import java.util.List;

/**
 * 文章服务
 *
 * <p>夜雨 创建于 2021-02-23 21:33
 */
public interface ArticleService extends BaseService<Article> {

	/**
	 * 获取每周阅读排行
	 *
	 * @return 热门文章列表
	 * @throws ServiceException 服务异常
	 */
	List<ArticleHot> getArticleHot() throws ServiceException;

	/**
	 * 更新阅读计数，包括触发每周热门排行统计，同一 IP 3 小时内访问多次的文章只计一次
	 *
	 * @param ip      访问者 IP
	 * @param article 访问文章
	 * @throws ServiceException 服务异常
	 */
	void read(String ip, Article article) throws ServiceException;
}