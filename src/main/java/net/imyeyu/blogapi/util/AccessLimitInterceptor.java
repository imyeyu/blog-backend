package net.imyeyu.blogapi.util;

import com.google.gson.Gson;
import net.imyeyu.blogapi.bean.AccessLimit;
import net.imyeyu.blogapi.bean.Response;
import net.imyeyu.blogapi.bean.ReturnCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * 访问控制（限制频率和登录权限）
 *
 * <p>夜雨 创建于 2021-07-20 16:46
 */
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {

	@Autowired
	private Gson gson;

	@Autowired
	private Token token;

	@Autowired
	private Redis<String, Long> redisAccessLimit;

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
		// 方法注解
		if (handler instanceof HandlerMethod handlerMethod) {
			AccessLimit accessLimit = handlerMethod.getMethodAnnotation(AccessLimit.class);
			if (accessLimit == null) {
				return true;
			}
			int ms = accessLimit.time();
			boolean needLogin = accessLimit.needLogin();
			// 需要登录
			if (needLogin) {
				boolean isLogin = this.token.isValid(req.getParameter("token"));
				if (!isLogin) {
					render(resp, new Response<>(ReturnCode.PERMISSION_MISS, "需要登陆"));
					return false;
				}
			}
			// 键
			String key = req.getRemoteAddr() + "#" + handlerMethod.getMethod().getName();
			// 该 IP 限时内是否访问过该方法
			if (redisAccessLimit.has(key) && 0D + System.currentTimeMillis() - redisAccessLimit.get(key) < ms) {
				render(resp, new Response<>(ReturnCode.REQUEST_BAD, "请求频率过高"));
				return false;
			}
			// 缓存记录
			redisAccessLimit.set(key, System.currentTimeMillis(), Duration.ofMillis(ms));
			return true;
		}
		return true;
	}

	/**
	 * 回调数据
	 *
	 * @param response 返回
	 * @param resp 返回结果
	 * @throws Exception 异常
	 */
	private void render(HttpServletResponse response, Response<?> resp)throws Exception {
		response.setContentType("application/json;charset=UTF-8");
		OutputStream out = response.getOutputStream();
		out.write(gson.toJson(resp).getBytes(StandardCharsets.UTF_8));
		out.flush();
		out.close();
	}
}