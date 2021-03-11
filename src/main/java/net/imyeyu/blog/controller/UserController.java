package net.imyeyu.blog.controller;

import com.google.gson.Gson;
import net.imyeyu.blog.entity.User;
import net.imyeyu.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
	
	@PostMapping("/login")
	public String doLogin(@RequestParam("user") String user, @RequestParam("password") String password) {
		return "";
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