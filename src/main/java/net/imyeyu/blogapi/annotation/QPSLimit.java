package net.imyeyu.blogapi.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * QPS：每秒访问限制，参数为访问间隔毫秒，-1 表示不限制，默认 1 秒
 *
 * <p>夜雨 创建于 2021-08-16 17:57
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface QPSLimit {

	int value() default 1000;
}