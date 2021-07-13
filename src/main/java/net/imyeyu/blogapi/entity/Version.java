package net.imyeyu.blogapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 版本管理
 *
 * <p>夜雨 创建于 2021-06-10 16:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Version extends BaseEntity {

	private String name;
	private String version;
	private String content;
	private String url;
}