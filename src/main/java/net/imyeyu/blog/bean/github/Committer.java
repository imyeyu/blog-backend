package net.imyeyu.blog.bean.github;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Github 提交记录对象
 *
 * 夜雨 创建于 2021-07-02 11:42
 */
@Data
@AllArgsConstructor
public class Committer {

	private String name;
	private Long commitedAt;
}