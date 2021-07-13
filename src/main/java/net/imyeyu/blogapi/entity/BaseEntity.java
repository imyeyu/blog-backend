package net.imyeyu.blogapi.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 基本实体
 *
 * <p>夜雨 创建于 2021-05-20 11:38
 */
@Data
public class BaseEntity implements Serializable {

	protected Long id;
	protected Long createdAt;
	protected Long updatedAt;
	protected Long deletedAt;
}