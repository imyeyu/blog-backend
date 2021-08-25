package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.bean.EmailType;
import net.imyeyu.blogapi.entity.EmailQueue;
import net.imyeyu.blogapi.entity.EmailQueueLog;

import java.util.List;

/**
 * 邮件推送队列
 *
 * <p>夜雨 创建于 2021-08-24 16:22
 */
public interface EmailQueueMapper extends BaseMapper<EmailQueue> {

	void addLog(EmailQueueLog log);

	List<EmailQueue> findAll();

	EmailQueue find(EmailType type, Long dataId);

	void delete(String UUID);
}