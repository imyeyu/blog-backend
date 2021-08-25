package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.EmailType;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.EmailQueue;
import net.imyeyu.blogapi.entity.EmailQueueLog;

import java.util.List;

/**
 * 邮件推送队列服务
 *
 * <p>夜雨 创建于 2021-08-24 16:20
 */
public interface EmailQueueService extends BaseService<EmailQueue> {

	/**
	 * 添加推送日志
	 *
	 * @param log 推送日志
	 * @throws ServiceException 服务异常
	 */
	void addLog(EmailQueueLog log) throws ServiceException;

	/**
	 * 根据推送类型和数据 ID 获取推送对象
	 *
	 * @param type   推送类型
	 * @param dataId 数据 ID
	 * @return 推送对象
	 * @throws ServiceException 服务异常
	 */
	EmailQueue find(EmailType type, Long dataId) throws ServiceException;

	/**
	 * 遍历推送队列
	 *
	 * @return 推送队列
	 * @throws ServiceException 服务异常
	 */
	List<EmailQueue> findAll() throws ServiceException;

	/**
	 * 移除队列
	 *
	 * @param UUID UUID
	 * @throws ServiceException 服务异常
	 */
	void delete(String UUID) throws ServiceException;
}