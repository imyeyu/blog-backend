package net.imyeyu.blogapi.config;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Gson 配置
 *
 * <p>夜雨 创建于 2021-07-21 17:09
 */
@Configuration
public class GsonConfig {

	@Bean
	public HttpMessageConverters customConverters() {
		Collection<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
		messageConverters.add(gsonHttpMessageConverter);
		return new HttpMessageConverters(true, messageConverters);
	}
}