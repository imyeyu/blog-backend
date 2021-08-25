package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.EmailType;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.EmailQueue;
import net.imyeyu.blogapi.entity.EmailQueueLog;
import net.imyeyu.blogapi.mapper.EmailQueueMapper;
import net.imyeyu.blogapi.service.EmailQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 邮件推送队列服务实现
 *
 * <p>夜雨 创建于 2021-08-24 16:21
 */
@Service
public class EmailQueueServiceImplement implements EmailQueueService {

	@Autowired
	private EmailQueueMapper mapper;

	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
	@Override
	public void addLog(EmailQueueLog log) throws ServiceException {
		mapper.addLog(log);
	}

	@Override
	public EmailQueue find(EmailType type, Long dataId) throws ServiceException {
		return mapper.find(type, dataId);
	}

	@Override
	public List<EmailQueue> findAll() throws ServiceException {
		return mapper.findAll();
	}

	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
	@Override
	public void create(EmailQueue emailQueue) throws ServiceException {
		mapper.create(emailQueue);
	}

	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
	@Override
	public void delete(String UUID) throws ServiceException {
		mapper.delete(UUID);
	}
}