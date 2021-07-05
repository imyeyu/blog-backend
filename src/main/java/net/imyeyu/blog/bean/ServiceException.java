package net.imyeyu.blog.bean;

import lombok.Getter;

/**
 * 服务异常
 *
 * <p>夜雨 创建于 2021-05-20 15:18
 */
public class ServiceException extends Exception {

	@Getter private final ReturnCode code;

	public ServiceException(ReturnCode code, String msg) {
		super(msg);
		this.code = code;
	}
}