package net.imyeyu.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 夜雨博客后端 API
 *
 * <p>夜雨 创建于 2021-02-23 21:35
 */
@SpringBootApplication
@MapperScan("net.imyeyu.blog.mapper")
@EnableTransactionManagement
public class BlogApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApiApplication.class, args);
	}
}