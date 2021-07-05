package net.imyeyu.blog;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * SpringBoot 序列化（外部 Tomcat 需要）
 *
 * <p>夜雨 创建于 2021-03-11 19:02
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BlogApiApplication.class);
	}
}