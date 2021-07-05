package net.imyeyu.blog.util;

import lombok.NoArgsConstructor;
import net.imyeyu.betterjava.Encode;
import net.imyeyu.blog.bean.ReturnCode;
import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.User;
import net.imyeyu.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.security.SecureRandom;

/**
 * <p>口令验证机制
 * <p>和密码摘要并不一样，密码摘要存于数据库，Token 用于和前端交流，在 UserService doSignin 执行成功后才会生成 Token 缓存
 * 
 * <p>夜雨 创建于 2021-03-11 19:02
 */
@NoArgsConstructor
@Component
public class Token {

	/** 用户令牌盐值 */
	@Value("${slat.user.token}")
	private String salt;

	@Autowired
	private Redis<Long, String> redisToken;

	@Autowired
	private UserService service;
	
	/**
	 * <p>验证令牌是否有效
	 * <p>一级验证 Session，二级验证 Redis，有效期为 24 小时（每次二级验证有效时会刷新这个时间，即 24 小时内不再访问则视为登出）
	 * <p>Session 存键为 user.uid，Redis 存键为 uid，值均为令牌
	 *
	 * @param uid   UID
	 * @param token 令牌
	 * @return true 时表示有效
	 */
	public boolean isValid(Long uid, String token) throws ServiceException {
		final String flag = "user." + uid;
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (sra != null) {
			// Session 验证
			HttpSession session = sra.getRequest().getSession();
			Object sessionToken = session.getAttribute(flag);
			if (token.equals(sessionToken)) {
				return true;
			}
			// Redis 验证
			if (token.equals(redisToken.get(uid))) {
				session.setAttribute("user." + uid, token);
				redisToken.set(uid, token, 24);
				return true;
			}
			return false;
		} else {
			throw new ServiceException(ReturnCode.REQUEST_BAD, ReturnCode.REQUEST_BAD.getComment());
		}
	}

	/**
	 * 清除缓存
	 *
	 * @param uid uid
	 * @return true 为清除成功
	 * @throws ServiceException 异常
	 */
	public boolean clear(Long uid) throws ServiceException {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (sra != null) {
			// Session
			sra.getRequest().getSession().removeAttribute("user." + uid);
			// Redis
			return redisToken.delete(uid);
		} else {
			throw new ServiceException(ReturnCode.REQUEST_BAD, ReturnCode.REQUEST_BAD.getComment());
		}
	}
	
	/**
	 * 随机生成令牌
	 *
	 * @param user 用户（含 ID、用户名）
	 * @return 令牌
	 */
	public String generate(User user) {
		return user.getId() + "#" + Encode.md5(user.getName() + salt + new SecureRandom().nextLong());
	}
}