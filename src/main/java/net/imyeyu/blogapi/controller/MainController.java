package net.imyeyu.blogapi.controller;

import net.imyeyu.betterjava.Encode;
import net.imyeyu.blogapi.annotation.AOPLog;
import net.imyeyu.blogapi.bean.Response;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.service.DynamicDataService;
import net.imyeyu.blogapi.service.FriendChainService;
import net.imyeyu.blogapi.service.VersionService;
import net.imyeyu.blogapi.util.Captcha;
import net.imyeyu.blogapi.util.GitHub;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 主接口（乱七八糟的接口）
 *
 * <p>夜雨 创建于 2021-02-23 21:38
 */
@RestController
@RequestMapping("/")
public class MainController extends BaseController {

	@Autowired
	private GitHub gitHub;

	@Autowired
	private VersionService versionService;

	@Autowired
	private FriendChainService friendChainService;

	@Autowired
	private DynamicDataService dynamicDataService;

	@RequestMapping("")
	public String root() {
		return "It is working!";
	}

	/**
	 * 获取验证码图像（返回 jpeg 图像流，缓存于 Session）
	 *
	 * @param params 需要 width 宽度，height 高度，from 来自模块
	 * @param request  请求体
	 * @param response 响应体
	 */
	@GetMapping("/captcha")
	public void getCaptcha(@RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) {
		// 返回图像流
		response.setContentType("image/png");
		response.setDateHeader("expries", -1);
		response.setHeader("Cache-Control", "no-cache"); // 禁止缓存
		response.setHeader("Pragma", "no-cache");
		try {
			// 宽度
			String width = params.get("width");
			if (ObjectUtils.isEmpty(width)) {
				ImageIO.write(Captcha.error(ReturnCode.PARAMS_MISS), "jpg", response.getOutputStream());
				return;
			} else if (!Encode.isNumber(width) || Integer.parseInt(width) < 64) {
				ImageIO.write(Captcha.error(ReturnCode.PARAMS_BAD), "jpg", response.getOutputStream());
				return;
			}
			// 高度
			String height = params.get("height");
			if (ObjectUtils.isEmpty(height)) {
				ImageIO.write(Captcha.error(ReturnCode.PARAMS_MISS), "jpg", response.getOutputStream());
				return;
			} else if (!Encode.isNumber(height) || Integer.parseInt(height) < 19) {
				ImageIO.write(Captcha.error(ReturnCode.PARAMS_BAD), "jpg", response.getOutputStream());
				return;
			}
			// 来自
			String from = params.get("from");
			if (ObjectUtils.isEmpty(from)) {
				ImageIO.write(Captcha.error(ReturnCode.PARAMS_MISS), "jpg", response.getOutputStream());
				return;
			}
			// 生成验证码
			Captcha captcha = new Captcha(Integer.parseInt(width), Integer.parseInt(height));
			// 缓存校验码
			request.getSession().setAttribute(from + "_CAPTCHA", captcha.getCode().toString());
			// 输出图像流
			ImageIO.write(captcha.getImage(), "jpg", response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
			try {
				ImageIO.write(Captcha.error(ReturnCode.ERROR), "jpg", response.getOutputStream());
			} catch (IOException subE) {
				subE.printStackTrace();
			}
		}
	}

	/** @return 所有友链 */
	@GetMapping("/friend-chain")
	public Response<?> getFriendChain() {
		try {
			return new Response<>(ReturnCode.SUCCESS, friendChainService.findAll());
		} catch (Exception e) {
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 获取软件最新版本状态
	 *
	 * @param name 软件名称
	 * @return 最新版本状态
	 */
	@AOPLog
	@GetMapping("/versions/{name}")
	public Response<?> getVersion(@PathVariable("name") String name) {
		if (StringUtils.isEmpty(name)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：name");
		}
		try {
			return new Response<>(ReturnCode.SUCCESS, versionService.findByName(name));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		}
	}

	/**
	 * 获取自定义动态数据
	 *
	 * @param key 键
	 * @return 动态数据
	 */
	@AOPLog
	@GetMapping("/dynamic/{key}")
	public Response<?> getDynamicData(@PathVariable("key") String key) {
		if (StringUtils.isEmpty(key)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：key");
		}
		try {
			return new Response<>(ReturnCode.SUCCESS, dynamicDataService.findByKey(key));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		}
	}

	/**
	 * 获取 Github 仓库提交记录（最近 24 条）
	 *
	 * @param user  用户
	 * @param repos 仓库
	 * @return 提交记录
	 */
	@GetMapping("/github/{user}/{repos}")
	public Response<?> getGitHubCommits(@PathVariable("user") String user, @PathVariable("repos") String repos) {
		if (StringUtils.isEmpty(user)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数: user");
		}
		if (StringUtils.isEmpty(repos)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数: repos");
		}
		try {
			return new Response<>(ReturnCode.SUCCESS, gitHub.getCommits(user, repos));
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, e);
		}
	}
}