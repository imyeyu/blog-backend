package net.imyeyu.blogapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户设置
 *
 * <p>夜雨 创建于 2021-08-12 15:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserConfig extends BaseEntity {

	private Long userId;
	private Boolean emailNotifyReply;

	public UserConfig(Long userId) {
		this.userId = userId;
		emailNotifyReply = true;
	}
}