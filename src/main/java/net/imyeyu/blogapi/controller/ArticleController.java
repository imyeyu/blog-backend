package net.imyeyu.blogapi.controller;

import net.imyeyu.blogapi.annotation.AOPLog;
import net.imyeyu.blogapi.annotation.QPSLimit;
import net.imyeyu.blogapi.bean.Response;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.Article;
import net.imyeyu.blogapi.service.ArticleClassService;
import net.imyeyu.blogapi.service.ArticleLabelService;
import net.imyeyu.blogapi.service.ArticleService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

	@Autowired
	private ArticleClassService classService;

	@Autowired
	private ArticleLabelService labelService;

	/**
	 * 获取
	 *
	 * @param id 文章 ID
	 * @return 文章
	 */
	@AOPLog
	@RequestMapping("/{id}")
	public Response<?> get(@PathVariable Long id, HttpServletRequest req) {
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

	/**
	 * 喜欢文章
	 * <p>限制每个 IP 1240 毫秒触发一次，不需要登录
	 *
	 * @param id 文章 ID
	 * @return 最新喜欢数量
	 */
	@AOPLog
	@QPSLimit
	@RequestMapping("/like/{id}")
	public Response<?> like(@PathVariable Long id) {
		if (ObjectUtils.isEmpty(id)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：id");
		}
		try {
			return new Response<>(ReturnCode.SUCCESS, service.like(id));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		}
	}

	/**
	 * 获取列表（简要）
	 *
	 * @param offset 查询偏移
	 * @return 列表
	 */
	@RequestMapping("/list")
	public Response<?> getMany(@RequestParam Long offset) {
		if (ObjectUtils.isEmpty(offset)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：offset");
		}
		try {
			List<Article> many = service.findMany(offset, 16);
			return new Response<>(ReturnCode.SUCCESS, many);
		} catch (ServiceException e) {
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 根据分类获取列表
	 *
	 * @param cid    分类 ID
	 * @param offset 查询偏移
	 * @return 列表
	 */
	@RequestMapping("/list/class")
	public Response<?> getManyByClass(@RequestParam Long cid, @RequestParam Long offset) {
		if (ObjectUtils.isEmpty(cid)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：cid");
		}
		if (ObjectUtils.isEmpty(offset)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：offset");
		}
		try {
			return new Response<>(ReturnCode.SUCCESS, service.findManyByClass(classService.find(cid), offset, 16));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, e);
		}
	}


	/**
	 * 根据标签获取列表
	 *
	 * @param lid    标签 ID
	 * @param offset 查询偏移
	 * @return 列表
	 */
	@RequestMapping("/list/label")
	public Response<?> getManyByLabel(@RequestParam Long lid, @RequestParam Long offset) {
		if (ObjectUtils.isEmpty(lid)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：lid");
		}
		if (ObjectUtils.isEmpty(offset)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：offset");
		}
		try {
			return new Response<>(ReturnCode.SUCCESS, service.findManyByLabel(labelService.find(lid), offset, 16));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/** @return 文章分类 */
	@RequestMapping("/classes")
	public Response<?> getClasses() {
		try {
			return new Response<>(ReturnCode.SUCCESS, classService.findBySide());
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		}
	}

	/** @return 每周访问排行榜 */
	@RequestMapping("/ranking")
	public Response<?> getRanking() {
		try {
			return new Response<>(ReturnCode.SUCCESS, service.getRanking());
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		}
	}
}