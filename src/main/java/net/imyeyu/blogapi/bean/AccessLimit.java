package net.imyeyu.blogapi.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 访问控制
 *
 * <p>夜雨 创建于 2021-07-20 16:48
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {

	// 每多少毫秒限制访问 1 次，默认 2 秒
	int time() default 2000;

	// 默认需要登陆（一般需要登陆权限才使用限速注解）
	boolean needLogin() default true;
}