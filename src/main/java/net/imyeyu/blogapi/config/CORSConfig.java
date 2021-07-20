package net.imyeyu.blogapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.Filter;
import java.util.Arrays;

/**
 * 跨域控制
 *
 * <p>夜雨 创建于 2021-05-14 09:21
 */
@Configuration
@EnableAutoConfiguration
public class CORSConfig {

	// 允许跨域的地址
	@Value("${cors.allow-origin}")
	private String allowOrigin;

	// 是否允许请求带有验证信息
	@Value("${cors.allow-credentials}")
	private boolean allowCredentials;

	// 允许请求的方法
	@Value("${cors.allow-methods}")
	private String allowMethods;

	// 允许服务端访问的客户端请求头
	@Value("${cors.allow-headers}")
	private String allowHeaders;

	@Bean
	public FilterRegistrationBean<Filter> corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.addAllowedHeader(allowHeaders);
		config.addAllowedMethod(allowMethods);
		config.setAllowCredentials(allowCredentials);
		config.setAllowedOriginPatterns(Arrays.asList(allowOrigin.split(",")));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}
}