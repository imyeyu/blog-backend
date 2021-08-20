package net.imyeyu.blogapi.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要验证令牌，只验证该令牌有没有登录，不验证获取的数据和令牌的关系
 *
 * <p>夜雨 创建于 2021-08-16 17:58
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RequiredToken {

	// true 需要登录
	boolean value() default true;
}