package net.imyeyu.blogapi.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体注解，Component 别名，只是为了在实体类注入服务接口
 *
 * <p>夜雨 创建于 2021-08-18 16:31
 */
@Component
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
	String value() default "";
}