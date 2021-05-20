package net.imyeyu.blog.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户
 *
 * 夜雨 创建于 2021/3/1 17:11
 */
@Data
public class User implements Serializable {

	private Long id;
	private String email;
	private String userName;
	private String password;
	private Long createdAt;
	private Long canceledAt;
	private Long deletedAt;
}