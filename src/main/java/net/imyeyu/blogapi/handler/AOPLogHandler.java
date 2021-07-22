package net.imyeyu.blogapi.handler;

import lombok.extern.slf4j.Slf4j;
import net.imyeyu.blogapi.bean.Response;
import net.imyeyu.blogapi.entity.BaseEntity;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * AOP 切面日志
 *
 * <p>夜雨 创建于 2021-07-21 23:48
 */
@Slf4j
@Aspect
@Component
public class AOPLogHandler {

	/** 注入注解 */
	@Pointcut("@annotation(net.imyeyu.blogapi.annotation.AOPLog)")
	public void logPointCut() {
	}

	/**
	 * 执行前
	 *
	 * @param joinPoint  切入点
	 * @throws Throwable 异常
	 */
	@Before("logPointCut()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		// 接收到请求，记录请求内容
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (sra != null) {
			log.info("");
			HttpServletRequest request = sra.getRequest();
			log.info("来自：" + request.getRemoteAddr());
			log.info("请求：" + request.getRequestURI());
			log.info("执行：" + joinPoint.getSignature().getDeclaringType().getSimpleName() + "." + joinPoint.getSignature().getName());
			return;
		}
		throw new IllegalAccessException("异常请求");
	}

	/**
	 * 执行后
	 *
	 * @param response 返回内容
	 * @throws Throwable 异常
	 */
	@AfterReturning(returning = "response", pointcut = "logPointCut()")
	public void doAfterReturning(Object response) throws Throwable {
		if (response instanceof Response<?> resp) {
			if (20000 < resp.getCode()) {
				log.error("异常：" + resp.getCode() + "." + resp.getMsg());
			} else {
				String msg = "返回：" + resp.getCode() + ".";
				if (resp.getData() instanceof BaseEntity entity) {
					// 返回实体
					msg += entity.getClass().getSimpleName() + "." + entity.getId();
				} else if (resp.getData() instanceof Boolean bool) {
					// 返回布尔值
					msg += bool;
				} else if (resp.getData() instanceof List<?> list) {
					// 返回数组
					if (list.isEmpty()) {
						msg += "List<?>.isEmpty";
					} else {
						msg += "List<" + list.get(0).getClass().getSimpleName() + ">." + list.size();
					}
				} else {
					// 其他对象
					msg += resp.getData().getClass().getSimpleName();
				}
				log.info(msg);
			}
		}
	}

	/**
	 * 环绕
	 *
	 * @param pjp 切入点
	 * @return 执行返回
	 * @throws Throwable 异常
	 */
	@Around("logPointCut()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		return pjp.proceed();
	}
}