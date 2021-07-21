package net.imyeyu.blogapi.config;

import net.imyeyu.blogapi.util.AccessLimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

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

	/**
	 * 过滤器
	 *
	 * @param registry 注册表
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// 访问控制
		registry.addInterceptor(accessLimitInterceptor).addPathPatterns("/**");
	}

	/**
	 * 通信消息转换
	 *
	 * @param converters 转换器
	 */
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new GsonHttpMessageConverter());
	}
}