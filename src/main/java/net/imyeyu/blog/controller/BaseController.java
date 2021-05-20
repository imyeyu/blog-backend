package net.imyeyu.blog.controller;

import lombok.Getter;

/**
 * 基本控制器
 *
 * 夜雨 创建于 2021/2/23 21:37
 */
public class BaseController {

	/**
	 * 返回代码
	 *
	 * 夜雨 创建于 2021-05-20 00:12
	 */
	@Getter
	public enum Code {
		SUCCESS    (20000, "执行成功"),
		MISS_PARAMS(40000, "缺少参数"),
		ERROR      (50000, "服务端异常");

		private final Integer code;
		private final String comment;

		Code(Integer code, String comment) {
			this.code = code;
			this.comment = comment;
		}
	}
}