package net.imyeyu.blog.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.imyeyu.betterjava.bean.BlogResponse;

/**
 * 返回前端数据对象
 *
 * <p>夜雨 创建于 2021-05-20 00:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Response<T> extends BlogResponse<T> {

	public Response(ReturnCode code) {
		super(code.getCode(), "", null);
	}

	public Response(ReturnCode code, Throwable e) {
		super(code.getCode(), e.getMessage(), null);
	}

	public Response(ReturnCode code, String msg) {
		super(code.getCode(), msg, null);
	}

	public Response(ReturnCode code, T data) {
		super(code.getCode(), "", data);
	}
}