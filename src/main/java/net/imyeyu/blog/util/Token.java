package net.imyeyu.blog.util;

import lombok.NoArgsConstructor;
import net.imyeyu.betterjava.Encode;
import net.imyeyu.blog.bean.ReturnCode;
import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.User;
import net.imyeyu.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
	private RedisTemplate<Long, String> redisUserToken;

	@Autowired
	private UserService service;
	
	/**
	 * <p>验证令牌是否有效
	 * <p>当 uid 所查找的令牌无效但构造的令牌对数据库验证有效时，所有验证机制变为有效，数据库是最后验证也是最高权限验证
	 * <p>Session 存键为 user.uid，Redis 存键为 uid，值均为令牌
	 *
	 * @param uid UID
	 * @return true 时表示有效
	 */
	public boolean isValid(long uid, String token) throws ServiceException {
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
			Redis<Long, String> rdToken = new Redis<>(redisUserToken);
			if (token.equals(rdToken.get(uid))) {
				session.setAttribute("user." + uid, token);
				rdToken.set(uid, token, 24);
				return true;
			}
			// 数据库验证
			User user = service.find(uid);
			if (token.equals(generate(user))) {
				session.setAttribute("user." + uid, token);
				rdToken.set(uid, token, 24);
				return true;
			}
			return false;
		} else {
			throw new ServiceException(ReturnCode.REQUEST_BAD, ReturnCode.REQUEST_BAD.getComment());
		}
	}
	
	/**
	 * <p>生成令牌
	 * <p>需要 ID、用户名和摘要后的密码
	 * <p>加盐《来自风平浪静的明天》
	 *
	 * @return 令牌
	 */
	public String generate(User user) {
		return user.getId() + "#" + Encode.md5(user.getName() + "Nagiasu" + user.getPassword());
	}
}