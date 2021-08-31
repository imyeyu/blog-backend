package net.imyeyu.blogapi.bean;

import lombok.Getter;

/**
 * 返回代码
 *
 * <p>夜雨 创建于 2021-05-21 14:32
 */
@Getter
public enum ReturnCode {

	// 200 正常
	SUCCESS            (20000, "执行成功"),
	// 400 参数异常
	PARAMS_MISS        (40000, "缺少参数"),
	PARAMS_BAD         (40001, "不合法的参数"),
	PARAMS_EXPIRD      (40002, "过期的参数"),
	// 401 权限异常
	PERMISSION_MISS    (40100, "无权限"),
	PERMISSION_ERROR   (40101, "无权限"),
	REQUEST_BAD        (40102, "非法请求"),
	// 403 数据异常
	DATA_EXIST         (40301, "数据已存在"),
	// 404 资源异常
	RESULT_NULL        (40400, "无数据"),
	RESULT_BAD         (40401, "返回数据异常"),
	RESULT_BAN         (40402, "禁用的数据"),
	RESULT_TIMEOUT     (40403, "上游服务器连接超时"),
	// 500 致命异常
	ERROR              (50000, "服务异常"),
	ERROR_NPE_METHOD   (50001, "服务异常"),
	ERROR_NPE_VARIABLE (50002, "服务异常"),
	ERROR_OFF_SERVICE  (50003, "服务关闭");

	private final Integer code;
	private final String comment;

	ReturnCode(Integer code, String comment) {
		this.code = code;
		this.comment = comment;
	}
}