package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.CommentRemindQueue;
import net.imyeyu.blogapi.mapper.CommentRemindQueueMapper;
import net.imyeyu.blogapi.service.CommentRemindQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论回复队列服务实现
 *
 * <p>夜雨 创建于 2021-08-25 00:11
 */
@Service
public class CommentRemindQueueServiceImplement implements CommentRemindQueueService {

	@Autowired
	private CommentRemindQueueMapper mapper;

	@Override
	public List<CommentRemindQueue> findManyByUID(Long uid) throws ServiceException {
		return mapper.findManyByUID(uid);
	}

	@Override
	public void deleteByUID(Long uid) throws ServiceException {
		mapper.deleteByUID(uid);
	}

	@Override
	public void deleteByRID(Long rid) throws ServiceException {
		mapper.deleteByRID(rid);
	}

	@Override
	public void create(CommentRemindQueue commentRemindQueue) throws ServiceException {
		mapper.create(commentRemindQueue);
	}
}