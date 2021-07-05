package net.imyeyu.blog.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 文章标签
 *
 * 夜雨 创建于 2021-07-04 10:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleLabel extends BaseEntity implements Serializable {

	private String name;
}