package net.imyeyu.blogapi.controller;

import net.imyeyu.blogapi.bean.Response;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.Article;
import net.imyeyu.blogapi.service.ArticleService;
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
 * <p>夜雨 创建于 2021-02-17 17:47
 */
@RestController
@RequestMapping("/article")
public class ArticleController extends BaseController {

	@Autowired
	private ArticleService service;

	/**
	 * 获取文章列表（简要）
	 *
	 * @param offset 查询偏移
	 * @return 文章列表
	 */
	@RequestMapping("")
	public Response<?> getArticles(@RequestParam Long offset) {
		if (ObjectUtils.isEmpty(offset)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：offset");
		}
		try {
			return new Response<>(ReturnCode.SUCCESS, service.findMany(offset, 16));
		} catch (ServiceException e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 获取指定文章
	 *
	 * @param id 文章 ID
	 * @return 文章
	 */
	@RequestMapping("/{id}")
	public Response<?> getArticle(@PathVariable Long id, HttpServletRequest req) {
		if (ObjectUtils.isEmpty(id)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：id");
		}
		try {
			Article article = service.find(id);
			service.read(req.getRemoteAddr(), article);
			return new Response<>(ReturnCode.SUCCESS, article);
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/** @return 每周访问排行榜 */
	@RequestMapping("/hot")
	public Response<?> getArticleHot() {
		try {
			return new Response<>(ReturnCode.SUCCESS, service.getArticleHot());
		} catch (ServiceException e) {
			e.printStackTrace();
			return new Response<>(e.getCode(), e);
		}
	}
}