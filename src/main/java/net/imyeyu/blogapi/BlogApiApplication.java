package net.imyeyu.blogapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <p>夜雨博客后端 API
 * <p>本端所有接口面向用户，不做管理接口，数据管理将使用 JavaFX
 *
 * <p>夜雨 创建于 2021-02-23 21:35
 */
@SpringBootApplication
@MapperScan("net.imyeyu.blogapi.mapper")
@EnableTransactionManagement
public class BlogApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApiApplication.class, args);
	}
}