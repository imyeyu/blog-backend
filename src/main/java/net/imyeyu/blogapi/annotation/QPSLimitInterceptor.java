package net.imyeyu.blogapi.annotation;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.imyeyu.blogapi.bean.Response;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.util.Redis;
import net.imyeyu.blogapi.util.Token;
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
 * 请求频率限制
 *
 * <p>夜雨 创建于 2021-08-16 18:07
 */
@Slf4j
@Component
public class QPSLimitInterceptor implements HandlerInterceptor {

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
			QPSLimit qpsLimit = handlerMethod.getMethodAnnotation(QPSLimit.class);
			if (qpsLimit == null) {
				return true;
			}
			// 频率限制
			int ms = qpsLimit.value();
			if (0 < ms) {
				// 键
				String key = req.getRemoteAddr() + "#" + handlerMethod.getMethod().getName();
				// 该 IP 限时内是否访问过该方法
				long cd;
				if (redisAccessLimit.has(key) && 0D + (cd = System.currentTimeMillis() - redisAccessLimit.get(key)) < ms) {
					log.warn("请求频率过高：" + key + ".CDMS" + (ms - cd));
					render(resp, new Response<>(ReturnCode.REQUEST_BAD, "请求频率过高"));
					return false;
				}
				// 缓存记录
				redisAccessLimit.set(key, System.currentTimeMillis(), Duration.ofMillis(ms));
			}
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