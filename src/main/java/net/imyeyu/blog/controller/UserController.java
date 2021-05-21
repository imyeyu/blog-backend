package net.imyeyu.blog.controller;

import net.imyeyu.betterjava.Encode;
import net.imyeyu.blog.bean.Response;
import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.User;
import net.imyeyu.blog.service.UserService;
import net.imyeyu.blog.util.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	 * @param params 含 user, password
	 * @return true 为已登录
	 */
	@PostMapping("/login")
	public Response<?> doLogin(@RequestBody Map<String, String> params) {
		if (StringUtils.isEmpty(params.get("user"))) {
			return new Response<>(Code.MISS_PARAMS, "请输入 UID、邮箱或用户名");
		}
		if (StringUtils.isEmpty(params.get("passowrd"))) {
			return new Response<>(Code.MISS_PARAMS, "请输入密码");
		}

		try {
			return new Response<>(Code.SUCCESS, service.doLogin(params.get("user"), params.get("password")));
		} catch (ServiceException e) {
			return new Response<>(Code.SUCCESS, e.getMessage());
		} catch (Exception e) {
			return new Response<>(Code.ERROR, e.getMessage());
		}
	}

	/**
	 * 返回某用户是否已登录
	 *
	 * @param params 含 uid 和 token
	 * @return true 为已登录
	 */
	@PostMapping("/login/status")
	public Response<?> isLogin(@RequestBody Map<String, String> params) {
		String uid = params.get("uid");
		if (StringUtils.isEmpty(uid)) {
			return new Response<>(Code.MISS_PARAMS, "缺少参数：uid");
		} else if (!Encode.isNumber(uid)) {
			return new Response<>(Code.MISS_PARAMS, "参数 uid 应该是个数字");
		}
		if (StringUtils.isEmpty(params.get("token"))) {
			return new Response<>(Code.MISS_PARAMS, "缺少参数：token");
		}
		return new Response<>(Code.SUCCESS, new Token(params.get("token")).isValid(Integer.parseInt(params.get("uid"))));
	}
	
	@PostMapping("/create")
	public String create(User user) {
		try {
			service.create(user);
			return "";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	@GetMapping("/delete")
	public String doDelete() {
//		System.out.println(Token.isValid(1));
		return "123";
	}
}