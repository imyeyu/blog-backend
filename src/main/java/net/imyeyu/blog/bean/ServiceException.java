package net.imyeyu.blog.bean;

/**
 * 服务异常
 *
 * 夜雨 创建于 2021-05-20 15:18
 */
public class ServiceException extends Exception {

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}
}