package net.imyeyu.blog.controller;

import net.imyeyu.blog.bean.Response;
import net.imyeyu.blog.bean.ReturnCode;
import net.imyeyu.blog.entity.Article;
import net.imyeyu.blog.service.ArticleService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
	public Response<?> getArticles(@RequestParam Long offset) {
		if (ObjectUtils.isEmpty(offset)) {
			return new Response<>(ReturnCode.MISS_PARAMS, "缺少参数：offset");
		}
		return new Response<>(ReturnCode.SUCCESS, service.findByList(offset, 16));
	}

	/**
	 * 获取指定文章
	 *
	 * @param id 文章 ID
	 * @return 文章
	 */
	@RequestMapping("/{id}")
	public Response<?> getArticle(@PathVariable long id, HttpServletRequest req) {
		Article article = service.find(id);
		service.read(req.getRemoteAddr(), article);
		return new Response<>(ReturnCode.SUCCESS, article);
	}

	/** @return 每周访问排行榜 */
	@RequestMapping("/hot")
	public Response<?> getArticleHot() {
		return new Response<>(ReturnCode.SUCCESS, service.getArticleHot());
	}
}