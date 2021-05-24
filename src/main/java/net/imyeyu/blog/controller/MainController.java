package net.imyeyu.blog.controller;

import net.imyeyu.betterjava.Encode;
import net.imyeyu.blog.bean.ReturnCode;
import net.imyeyu.blog.util.Captcha;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 主接口
 *
 * 夜雨 创建于 2021/2/23 21:38
 */
@RestController
@RequestMapping("/")
public class MainController extends BaseController {

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
}