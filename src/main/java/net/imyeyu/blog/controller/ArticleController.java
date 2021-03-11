package net.imyeyu.blog.controller;

import net.imyeyu.blog.entity.Article;
import net.imyeyu.blog.entity.ArticleHot;
import net.imyeyu.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文章接口
 *
 * 夜雨 创建于 2021/2/17 17:47
 */
@RestController
@RequestMapping("/article")
public class ArticleController extends BaseController {

	@Autowired
	private ArticleService service;

	/**
	 * 获取文章（简要）
	 *
	 * @param offset 查询偏移
	 * @return 文章列表
	 */
	@RequestMapping("")
	public List<Article> getArticles(long offset) {
		return service.find(offset, 16);
	}

	/**
	 * 获取指定文章
	 *
	 * @param id 文章 ID
	 * @return 文章
	 */
	@RequestMapping("/{id}")
	public Article getArticle(@PathVariable long id, HttpServletRequest req) {
		Article article = service.findById(id);
		service.read(req.getRemoteAddr(), article);
		return article;
	}

	/** @return 每周访问排行榜 */
	@RequestMapping("/hot")
	public List<ArticleHot> getArticleHot() {
		return service.getArticleHot();
	}
}