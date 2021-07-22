package net.imyeyu.blogapi.handler;

import lombok.extern.slf4j.Slf4j;
import net.imyeyu.blogapi.bean.Response;
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
	 * 指定当前执行方法在 logPointCut 之前执行
	 *
	 * @param joinPoint  切入点
	 * @throws Throwable 异常
	 */
	@Before("logPointCut()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		// 接收到请求，记录请求内容
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (sra != null) {
			HttpServletRequest request = sra.getRequest();
			log.info(request.getRemoteAddr() + " 请求接口 " + request.getRequestURI());
			log.info("执行：" + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
			return;
		}
		throw new IllegalAccessException("异常请求");
	}

	/**
	 * 指定在方法之后返回
	 *
	 * @param response 返回内容
	 * @throws Throwable 异常
	 */
	@AfterReturning(returning = "response", pointcut = "logPointCut()")
	public void doAfterReturning(Object response) throws Throwable {
		if (response instanceof Response<?> resp) {
			if (20000 < resp.getCode()) {
				log.error("返回异常：" + resp.getMsg());
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