package net.imyeyu.blogapi.entity;

import lombok.Data;
import net.imyeyu.blogapi.bean.EmailType;

/**
 * 邮件队列
 *
 * <p>夜雨 创建于 2021-08-24 14:59
 */
@Data
public class EmailQueue {

	private String UUID;
	private EmailType type;
	private Long dataId;
	private Long sendAt;

	public EmailQueue() {
		UUID = java.util.UUID.randomUUID().toString();
	}
}