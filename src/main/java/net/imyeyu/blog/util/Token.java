package net.imyeyu.blog.util;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import net.imyeyu.betterjava.Encode;
import net.imyeyu.blog.entity.User;
import net.imyeyu.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 口令验证机制
 * 
 * @author 夜雨
 *
 */
public class Token {
	
	@Autowired
	private RedisTemplate<String, String> redis;
	
	@Autowired
	private UserService service;
	
	private final String token;
	
	public Token(String token) {
		this.token = token;
	}
	
	/**
	 * 管理员登录
	 * 
	 * @return true 时表示管理员登陆
	 */
	public boolean isValidAdmin() {
		return isValid(1);
	}
	
	/**
	 * 验证 Token 是否有效
	 * 
	 * @return true 时表示有效
	 */
	public boolean isValid(long uid) {
		boolean isValid = false;
		String flag = "user." + uid;
		// Session 验证
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpSession session = sra.getRequest().getSession();
		Object sessionToken = session.getAttribute(flag);
		if (sessionToken != null) {
			if (token.equals(sessionToken)) {
				isValid = true; // session 通过
			}
		}
		// Redis 验证
		if (!isValid) {
			String redisToken = redis.opsForValue().get(flag);
			if (token.equals(redisToken)) {
				isValid = true; // redis 通过
				session.setAttribute("user." + uid, token);
			}
		}
		// 数据库验证
		if (!isValid) {
			if (token.equals(generate(service.find(uid)))) {
				isValid = true; // 数据库通过
				session.setAttribute("user." + uid, token);
				redis.opsForValue().set("user." + uid, token, 2, TimeUnit.MINUTES);
			}
		}
		return isValid;
	}
	
	/**
	 * 生成 Token
	 * 
	 * @param user 用户
	 * @return Token
	 */
	public static String generate(User user) {
		return user.getId() + "#" + Encode.md5(user.getUserName() + "3.012+" + user.getPassword());
	}
}