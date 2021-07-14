package net.imyeyu.blogapi.util;

import lombok.NoArgsConstructor;
import net.imyeyu.betterjava.Encode;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.User;
import net.imyeyu.blogapi.service.UserService;
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
	 * @param token 令牌
	 * @return true 时表示有效
	 */
	public boolean isValid(String token) throws ServiceException {
		try {
			// 截取 UID
			Long uid = Long.parseLong(token.substring(0, token.indexOf("#")));
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
		} catch (Exception e) {
			throw new ServiceException(ReturnCode.PARAMS_BAD, "无效的令牌");
		}
	}

	/**
	 * 清除缓存
	 *
	 * @param token 令牌
	 * @return true 为清除成功
	 * @throws ServiceException 异常
	 */
	public boolean clear(String token) throws ServiceException {
		try {
			// 截取 UID
			Long uid = Long.parseLong(token.substring(0, token.indexOf("#")));
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if (sra != null) {
				// Session
				sra.getRequest().getSession().removeAttribute("user." + uid);
				// Redis
				return redisToken.destroy(uid);
			} else {
				throw new ServiceException(ReturnCode.REQUEST_BAD, ReturnCode.REQUEST_BAD.getComment());
			}
		} catch (Exception e) {
			throw new ServiceException(ReturnCode.PARAMS_BAD, "无效的令牌");
		}
	}
	
	/**
	 * <p>随机生成令牌
	 * <p>结果示例：32#69290ea91da44cd76a595af2a447a7d8
	 *
	 * @param user 用户（含 ID、用户名）
	 * @return 令牌
	 */
	public String generate(User user) {
		return user.getId() + "#" + Encode.md5(user.getName() + salt + new SecureRandom().nextLong());
	}
}