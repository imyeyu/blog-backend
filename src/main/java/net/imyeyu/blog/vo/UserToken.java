package net.imyeyu.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户令牌
 *
 * 夜雨 创建于 2021-05-25 23:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserToken {

	private Long id;
	private String name;
	private String token;
}