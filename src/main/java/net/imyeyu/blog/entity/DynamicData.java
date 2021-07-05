package net.imyeyu.blog.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 自定义动态数据
 *
 * <p>夜雨 创建于 2021-07-01 19:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DynamicData extends BaseEntity implements Serializable {

	private String key;
	private String value;
	private String comment;
}