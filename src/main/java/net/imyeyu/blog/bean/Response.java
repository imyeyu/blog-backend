package net.imyeyu.blog.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.imyeyu.blog.controller.BaseController;

/**
 * 返回前端数据对象
 *
 * 夜雨 创建于 2021-05-20 00:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

	private Integer code;
	private String msg;
	private T data;

	public Response(BaseController.Code code) {
		this(code.getCode(), "", null);
	}

	public Response(BaseController.Code code, String msg) {
		this(code.getCode(), msg, null);
	}

	public Response(BaseController.Code code, T data) {
		this(code.getCode(), "", data);
	}
}