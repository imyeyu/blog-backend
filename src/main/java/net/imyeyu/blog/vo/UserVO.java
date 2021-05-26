package net.imyeyu.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户
 *
 * 夜雨 创建于 2021-05-25 23:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

	private Long id;
	private String email;
	private String name;
	private String token;
}