package net.imyeyu.blog.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 文章分类
 *
 * <p>夜雨 创建于 2021-07-04 09:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleClass extends BaseEntity implements Serializable {

	private String name;
	private boolean isOther; // true 为其他类型文章，前端表现为在二级菜单
	private int order;       // 排序
}