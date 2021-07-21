package net.imyeyu.blogapi.config;

import net.imyeyu.blogapi.util.AccessLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 系统配置
 *
 * <p>夜雨 创建于 2021-07-20 16:44
 */
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Autowired
	private AccessLimitInterceptor accessLimitInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 访问控制
		registry.addInterceptor(accessLimitInterceptor).addPathPatterns("/**");
	}
}