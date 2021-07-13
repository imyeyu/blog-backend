package net.imyeyu.blogapi.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 含验证码数据实体
 *
 * <p>夜雨 创建于 2021-03-01 17:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaData<T> {

	private String captcha;
	private T data;
}