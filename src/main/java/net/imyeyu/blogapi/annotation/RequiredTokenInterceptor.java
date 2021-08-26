package net.imyeyu.blogapi.annotation;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.imyeyu.blogapi.bean.Response;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.util.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 验证令牌注解逻辑
 *
 * <p>夜雨 创建于 2021-08-16 18:07
 */
@Slf4j
@Component
public class RequiredTokenInterceptor implements HandlerInterceptor {

	@Autowired
	private Gson gson;

	@Autowired
	private Token token;

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
		// 方法注解
		if (handler instanceof HandlerMethod handlerMethod) {
			RequiredToken requiredToken = handlerMethod.getMethodAnnotation(RequiredToken.class);
			if (requiredToken == null) {
				return true;
			}
			// 需要令牌
			if (requiredToken.value()) {
				try {
					String token = req.getHeader("Token");
					if (StringUtils.isEmpty(token)) {
						render(resp, new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：Token"));
						return false;
					}
					if (!this.token.isValid(token)) {
						render(resp, new Response<>(ReturnCode.PERMISSION_MISS, "无效的令牌，无权限操作"));
						return false;
					}
				} catch (ServiceException e) {
					render(resp, new Response<>(e.getCode(), e.getMessage()));
				}
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