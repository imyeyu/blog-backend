package net.imyeyu.blog.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>夜雨 创建于 2021-07-03 12:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GithubCommit {

	private String name;
	private String msg;
	private String url;
	private Long commitedAt;
}