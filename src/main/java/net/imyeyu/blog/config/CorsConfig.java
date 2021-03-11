package net.imyeyu.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * 跨域配置
 *
 * 夜雨 创建于 2021/2/23 21:36
 */
@Configuration
public class CorsConfig {

	@Bean
	public CorsFilter corsFilter() {
		// 允许跨域的地址
		List<String> allowedOriginPatterns = new ArrayList<>();
		allowedOriginPatterns.add("*");

		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOriginPatterns(allowedOriginPatterns);
		config.setAllowCredentials(true);
		config.addAllowedMethod("*");
		config.addAllowedHeader("*");
		UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
		configSource.registerCorsConfiguration("/**", config);
		return new CorsFilter(configSource);
	}
}