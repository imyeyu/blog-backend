package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 抽象服务
 *
 * <p>夜雨 创建于 2021-08-05 01:58
 */
public abstract class AbstractService {

	/** @return Servlet 请求属性 */
	protected final ServletRequestAttributes getServletRequestAttributes() throws ServiceException {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (sra != null) {
			return sra;
		}
		throw new ServiceException(ReturnCode.ERROR_NPE_VARIABLE, ReturnCode.ERROR_NPE_VARIABLE.getComment());
	}

	/** @return HttpServlet 请求 */
	protected final HttpServletRequest getRequest() throws ServiceException {
		return getServletRequestAttributes().getRequest();
	}

	/** @return HttpServlet 回调 */
	protected final HttpServletResponse getResponse() throws ServiceException {
		return getServletRequestAttributes().getResponse();
	}

	/** @return Http 会话 */
	protected final HttpSession getSession() throws ServiceException {
		return getRequest().getSession();
	}

	/**
	 * 获取会话数据（字符串）
	 *
	 * @param key 键
	 * @return 值
	 * @throws ServiceException 服务异常
	 */
	protected final String getSessionData(String key) throws ServiceException {
		return (String) getSession().getAttribute(key);
	}

	/**
	 * 获取会话数据
	 *
	 * @param key   键
	 * @param clazz 值类型
	 * @param <T>   值类型
	 * @return 值
	 * @throws ServiceException 服务异常
	 */
	protected final <T>T getSessionData(String key, Class<T> clazz) throws ServiceException {
		return clazz.cast(getSession().getAttribute(key));
	}

	/**
	 * 设置会话数据
	 *
	 * @param key 键
	 * @param t   值
	 * @param <T> 值类型
	 * @throws ServiceException 服务异常
	 */
	protected final <T> void setSessionData(String key, T t) throws ServiceException {
		getSession().setAttribute(key, t);
	}

	/** @return 请求 IP */
	protected String getIP() throws ServiceException {
		return getRequest().getRemoteAddr();
	}
}