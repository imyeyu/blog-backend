package net.imyeyu.blog.bean;

import lombok.Getter;

/**
 * 返回代码
 *
 * 夜雨 创建于 2021-05-21 14:32
 */
@Getter
public enum ReturnCode {

	SUCCESS         (20000, "执行成功"),
	MISS_PARAMS     (40000, "缺少参数"),
	BAD_PARAMS      (40001, "不合法的参数"),
	MISS_PERMISSION (40100, "无权限"),
	ERROR_PERMISSION(40101, "无权限"),
	NULL_RESULT     (40400, "无结果"),
	ERROR           (50000, "服务端异常");

	private final Integer code;
	private final String comment;

	ReturnCode(Integer code, String comment) {
		this.code = code;
		this.comment = comment;
	}
}