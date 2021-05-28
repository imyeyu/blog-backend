package net.imyeyu.blog.util;

import lombok.NoArgsConstructor;
import net.imyeyu.betterjava.Encode;
import net.imyeyu.blog.bean.ReturnCode;
import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.User;
import net.imyeyu.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * <p>口令验证机制
 * <p>和密码摘要并不一样，密码摘要存于数据库，Token 用于和前端交流，在 UserService doSignin 执行成功后才会生成 Token 缓存
 * 
 * @author 夜雨
 */
@NoArgsConstructor
@Component
public class Token {

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
				System.out.println("session pass");
				return true;
			}
			// Redis 验证
			if (token.equals(redisToken.get(uid))) {
				session.setAttribute("user." + uid, token);
				redisToken.set(uid, token, 24);
				System.out.println("redis pass");
				return true;
			}
			return false;
		} else {
			throw new ServiceException(ReturnCode.REQUEST_BAD, ReturnCode.REQUEST_BAD.getComment());
		}
	}
	
	/**
	 * <p>随机生成令牌
	 * <p>加盐《来自风平浪静的明天》
	 *
	 * @param user 用户（含 ID、用户名和摘要后的密码）
	 * @return 令牌
	 */
	public String generate(User user) {
		return user.getId() + "#" + Encode.md5(user.getName() + "Nagiasu" + user.getPassword() + Math.random());
	}
}