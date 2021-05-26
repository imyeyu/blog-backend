package net.imyeyu.blog.entity;

import lombok.Data;

/**
 * 基本实体
 *
 * 夜雨 创建于 2021-05-20 11:38
 */
@Data
public class BaseEntity {

	protected Long id;
	protected Long createdAt;
	protected Long updatedAt;
	protected Long deletedAt;
}