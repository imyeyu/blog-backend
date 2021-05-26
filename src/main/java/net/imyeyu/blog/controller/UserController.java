package net.imyeyu.blog.controller;

import net.imyeyu.betterjava.Encode;
import net.imyeyu.blog.bean.CaptchaData;
import net.imyeyu.blog.bean.Response;
import net.imyeyu.blog.bean.ReturnCode;
import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.User;
import net.imyeyu.blog.service.UserService;
import net.imyeyu.blog.util.Captcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户接口
 *
 * 夜雨 创建于 2021/2/23 21:38
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
	
	@Autowired
	private UserService service;

	/**
	 * <p>用户登录
	 * <p>user 可以是 UID、邮箱或用户名
	 *
	 * @param params  含 user, password 和 captcha
	 * @param request 请求体
	 * @return true 为已登录
	 */
	@PostMapping("/sign-in")
	public Response<?> doSignIn(@RequestBody Map<String, String> params, HttpServletRequest request) {
		// 用户
		if (StringUtils.isEmpty(params.get("user"))) {
			return new Response<>(ReturnCode.PARAMS_MISS, "请输入 UID、邮箱或用户名");
		}
		// 密码
		if (StringUtils.isEmpty(params.get("password"))) {
			return new Response<>(ReturnCode.PARAMS_MISS, "请输入密码");
		}
		// 验证码
		try {
			Captcha.test(params.get("captcha"), "SIGNIN");
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		}
		// 执行登录
		try {
			return new Response<>(ReturnCode.SUCCESS, service.doSignIn(params.get("user"), params.get("password")));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, e.getMessage());
		}
	}

	/**
	 * 返回该 ID 用户是否已登录
	 *
	 * @param params 含 uid 和 token
	 * @return true 为已登录
	 */
	@PostMapping("/sign-in/status")
	public Response<?> isSignedIn(@RequestBody Map<String, String> params) {
		String uid = params.get("uid");
		if (StringUtils.isEmpty(uid)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：uid");
		} else if (!Encode.isNumber(uid)) {
			return new Response<>(ReturnCode.PARAMS_BAD, "参数 uid 应该是个数字");
		}
		if (StringUtils.isEmpty(params.get("token"))) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：token");
		}
		try {
			return new Response<>(ReturnCode.SUCCESS, service.isSignedIn(Long.parseLong(uid), params.get("token")));
		} catch (ServiceException e) {
			e.printStackTrace();
			return new Response<>(e.getCode(), e.getMessage());
		}
	}

	/**
	 * 注册用户
	 *
	 * @param request     请求体
	 * @param captchaData 含数据体和验证码请求对象，数据体为 User，至少包含用户名和密码
	 * @return true 为注册成功
	 */
	@PostMapping("/register")
	public Response<?> register(@RequestBody CaptchaData<User> captchaData, HttpServletRequest request) {
		// 用户
		User user = captchaData.getData();
		if (StringUtils.isEmpty(user.getName())) {
			return new Response<>(ReturnCode.PARAMS_MISS, "请输入用户名");
		}
		// 密码
		if (StringUtils.isEmpty(user.getPassword())) {
			return new Response<>(ReturnCode.PARAMS_MISS, "请输入密码");
		}
		// 验证码
		try {
			Captcha.test(captchaData.getCaptcha(), "REGISTER");
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		}
		// 创建用户
		try {
			service.create(user);
			return new Response<>(ReturnCode.SUCCESS, user);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, e.getMessage());
		}
	}
	
	@GetMapping("/delete")
	public String doDelete() {
		return "123";
	}
}