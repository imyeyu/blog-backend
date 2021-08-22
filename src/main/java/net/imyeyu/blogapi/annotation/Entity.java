package net.imyeyu.blogapi.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体注解，Component 别名，只是为了在实体类注入服务接口（如果实体需要注入服务，需要这个类注解）
 * <pre>
 *
 *  &#64;Entity
 *  &#64;NoArgsConstructor // 需要个空的构造方法让 MyBatis 正常实例化
 *  public class Entity {
 *
 *      &#64;Transient
 *      private transient static Service service;
 *
 *      // 通过构造方法注入
 *      &#64;Autowired
 *      public Entity(Service service) {
 *          Entity.service = service;
 *      }
 *  }
 *
 * </pre>
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