package net.imyeyu.blogapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.imyeyu.blogapi.bean.EmailType;

/**
 * <p>夜雨 创建于 2021-08-24 18:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmailQueueLog extends BaseEntity {

	private String UUID;
	private EmailType type;
	private Long dataId;
	private String sendTo;
	private Long sendAt;

	private Boolean isSent;
	private String exceptionMsg;
}