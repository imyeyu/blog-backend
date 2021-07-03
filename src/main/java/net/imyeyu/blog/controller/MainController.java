package net.imyeyu.blog.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.imyeyu.betterjava.Encode;
import net.imyeyu.betterjava.Network;
import net.imyeyu.blog.bean.Response;
import net.imyeyu.blog.bean.ReturnCode;
import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.bean.github.Commit;
import net.imyeyu.blog.bean.github.Committer;
import net.imyeyu.blog.service.DynamicDataService;
import net.imyeyu.blog.service.VersionService;
import net.imyeyu.blog.util.Captcha;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * 主接口（乱七八糟的接口）
 *
 * 夜雨 创建于 2021/2/23 21:38
 */
@RestController
@RequestMapping("/")
public class MainController extends BaseController {

	@Autowired
	private VersionService versionService;

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
		} catch (IOException e) {
			e.printStackTrace();
			try {
				ImageIO.write(Captcha.error(ReturnCode.ERROR), "jpg", response.getOutputStream());
			} catch (IOException subE) {
				subE.printStackTrace();
			}
		}
	}

	/**
	 * 获取软件最新版本状态
	 *
	 * @param name 软件名称
	 * @return 最新版本状态
	 */
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
	public Response<?> getGithubCommits(@PathVariable("user") String user, @PathVariable("repos") String repos) {
		if (StringUtils.isEmpty(user)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数: user");
		}
		if (StringUtils.isEmpty(repos)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数: repos");
		}
		try {
			String response = Network.doGet("https://api.github.com/repos/" + user + "/" + repos + "/commits");
			JsonArray commits = JsonParser.parseString(response).getAsJsonArray();

			JsonObject commit, committer;
			String msg, url, name, date;

			final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			List<Commit> result = new ArrayList<>();
			for (int i = 0, l = commits.size(); i < l && i < 24; i++) {
				// HTML URL
				commit = commits.get(i).getAsJsonObject();
				url = commit.get("html_url").getAsString();
				// 提交说明
				commit = commit.get("commit").getAsJsonObject();
				msg = commit.get("message").getAsString();
				// 提交者
				committer = commit.get("committer").getAsJsonObject();
				name = committer.get("name").getAsString();
				date = committer.get("date").getAsString();

				result.add(new Commit(msg, url, new Committer(name, dateFormat.parse(date).getTime())));
			}
			return new Response<>(ReturnCode.SUCCESS, result);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, e);
		}
	}
}