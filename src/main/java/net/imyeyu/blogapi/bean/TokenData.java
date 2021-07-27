package net.imyeyu.blogapi.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>携带登录令牌的通信数据
 *
 * <p>夜雨 创建于 2021-07-14 10:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenData<T> {

	private String token;
	private T data;
}