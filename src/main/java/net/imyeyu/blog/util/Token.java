package net.imyeyu.blog.util;

import lombok.NoArgsConstructor;
import net.imyeyu.betterjava.Encode;
import net.imyeyu.blog.entity.User;
import net.imyeyu.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * 口令验证机制
 * 
 * @author 夜雨
 *
 */
@NoArgsConstructor
public class Token {
	
	@Autowired
	private RedisTemplate<Long, String> redisUserToken;
	
	@Autowired
	private UserService service;
	
	private String token;
	
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
	 * <p>验证 Token 是否有效
	 * <p>当 uid 所查找的 Token 无效但构造的 token 对数据库验证有效时，所有验证机制变为有效，数据库是最后验证也是最高权限验证
	 * <p>Session 存键为 user.uid，Redis 存键为 uid，值均为 Token
	 *
	 * @param uid UID
	 * @return true 时表示有效
	 */
	public boolean isValid(long uid) {
		final String flag = "user." + uid;
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (sra != null) {
			// Session 验证
			HttpSession session = sra.getRequest().getSession();
			Object sessionToken = session.getAttribute(flag);
			if (token.equals(sessionToken)) {
				return true;
			}
			RedisUtil<Long, String> rdToken = new RedisUtil<>(redisUserToken);
			// Redis 验证
			if (token.equals(rdToken.get(uid))) {
				session.setAttribute("user." + uid, token);
				return true;
			}
			// 数据库验证
			if (token.equals(generate(service.find(uid)))) {
				session.setAttribute("user." + uid, token);
				rdToken.set(uid, token, 1);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 生成 Token
	 * 
	 * @param user 用户
	 * @return Token
	 */
	public static String generate(User user) {
		return user.getId() + "#" + Encode.md5(user.getName() + "3.012+" + user.getPassword());
	}
}