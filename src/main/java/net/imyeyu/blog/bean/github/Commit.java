package net.imyeyu.blog.bean.github;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Github 提交更新信息
 *
 * 夜雨 创建于 2021-07-02 11:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commit {

	private String msg;
	private String url;

	private Committer committer;
}