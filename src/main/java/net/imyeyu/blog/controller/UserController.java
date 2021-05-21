package net.imyeyu.blog.controller;

import net.imyeyu.betterjava.Encode;
import net.imyeyu.blog.bean.Response;
import net.imyeyu.blog.bean.ReturnCode;
import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.User;
import net.imyeyu.blog.service.UserService;
import net.imyeyu.blog.util.Captcha;
import net.imyeyu.blog.util.Token;
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
	 * @param params 含 user, password 和 captcha
	 * @return true 为已登录
	 */
	@PostMapping("/signin")
	public Response<?> doSignin(@RequestBody Map<String, String> params, HttpServletRequest request) {
		if (StringUtils.isEmpty(params.get("user"))) {
			return new Response<>(ReturnCode.MISS_PARAMS, "请输入 UID、邮箱或用户名");
		}
		if (StringUtils.isEmpty(params.get("password"))) {
			return new Response<>(ReturnCode.MISS_PARAMS, "请输入密码");
		}
		if (StringUtils.isEmpty(params.get("captcha")) || !Captcha.isValid(request.getSession(), params.get("captcha"), "SIGNIN")) {
			return new Response<>(ReturnCode.BAD_PARAMS, "验证码错误");
		}
		try {
			return new Response<>(ReturnCode.SUCCESS, service.doSignin(params.get("user"), params.get("password")));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, e.getMessage());
		}
	}

	/**
	 * 返回某用户是否已登录
	 *
	 * @param params 含 uid 和 token
	 * @return true 为已登录
	 */
	@PostMapping("/signin/status")
	public Response<?> isLogin(@RequestBody Map<String, String> params) {
		String uid = params.get("uid");
		if (StringUtils.isEmpty(uid)) {
			return new Response<>(ReturnCode.MISS_PARAMS, "缺少参数：uid");
		} else if (!Encode.isNumber(uid)) {
			return new Response<>(ReturnCode.BAD_PARAMS, "参数 uid 应该是个数字");
		}
		if (StringUtils.isEmpty(params.get("token"))) {
			return new Response<>(ReturnCode.MISS_PARAMS, "缺少参数：token");
		}
		return new Response<>(ReturnCode.SUCCESS, new Token(params.get("token")).isValid(Integer.parseInt(params.get("uid"))));
	}

	/**
	 * 注册用户
	 *
	 * @param user 用户（至少包含 name、password 和 captcha）
	 * @return true 为注册成功
	 */
	@PostMapping("/register")
	public Response<?> register(@RequestBody User user) {
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