package net.imyeyu.blogapi.service.implement;

import net.imyeyu.betterjava.Time;
import net.imyeyu.blogapi.bean.EmailType;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.CommentRemindQueue;
import net.imyeyu.blogapi.entity.CommentReply;
import net.imyeyu.blogapi.entity.EmailQueue;
import net.imyeyu.blogapi.entity.User;
import net.imyeyu.blogapi.mapper.CommentReplyMapper;
import net.imyeyu.blogapi.service.CommentRemindQueueService;
import net.imyeyu.blogapi.service.CommentReplyService;
import net.imyeyu.blogapi.service.EmailQueueService;
import net.imyeyu.blogapi.service.UserConfigService;
import net.imyeyu.blogapi.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * 评论回复服务实现
 *
 * <p>夜雨 创建于 2021-08-24 10:34
 */
@Service
public class CommentReplyServiceImplement implements CommentReplyService {

	@Autowired
	private UserService userService;

	@Autowired
	private UserConfigService userConfigService;

	@Autowired
	private CommentRemindQueueService commentRemindQueueService;

	@Autowired
	private EmailQueueService emailQueueService;

	@Autowired
	private CommentReplyMapper mapper;

	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
	@Override
	public void deleteByUID(Long uid) throws ServiceException {
		mapper.deleteByUID(uid);
	}

	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
	@Override
	public void deleteByCID(Long cid) throws ServiceException {
		mapper.deleteByCID(cid);
	}

	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
	@Override
	public void create(CommentReply commentReply) throws ServiceException {
		commentReply.setCreatedAt(System.currentTimeMillis());
		mapper.create(commentReply);
		// 被回复者是注册用户
		if (!ObjectUtils.isEmpty(commentReply.getReceiverId())) {
			// 回复和被回复不是同一对象
			if (!commentReply.getReceiverId().equals(commentReply.getSenderId())) {
				User user = userService.find(commentReply.getReceiverId()).withConfig();
				// 被回复允许推送邮件
				if (!StringUtils.isEmpty(user.getEmail()) && user.getConfig().getEmailNotifyReply()) {
					// 添加提醒队列
					CommentRemindQueue remindQueue = new CommentRemindQueue();
					remindQueue.setUUID(UUID.randomUUID().toString());
					remindQueue.setUserId(commentReply.getReceiverId());
					remindQueue.setReplyId(commentReply.getId());
					commentRemindQueueService.create(remindQueue);
					// 邮件队列
					EmailQueue emailQueue = emailQueueService.find(EmailType.REPLY_REMINAD, commentReply.getReceiverId());
					if (emailQueue == null) {
						emailQueue = new EmailQueue();
						emailQueue.setUUID(UUID.randomUUID().toString());
						emailQueue.setType(EmailType.REPLY_REMINAD);
						emailQueue.setDataId(commentReply.getReceiverId());

						long now = System.currentTimeMillis();
						long H10 = Time.H * 10;
						if (now < Time.today() + H10) {
							emailQueue.setSendAt(Time.today() + H10);
						} else {
							emailQueue.setSendAt(Time.tomorrow() + H10);
						}
						emailQueueService.create(emailQueue);
					}
				}
			}
		}
	}

	@Override
	public CommentReply find(Long id) throws ServiceException {
		return mapper.find(id);
	}

	@Override
	public List<CommentReply> findMany(Long cid, Long offset, int limit) throws ServiceException {
		return mapper.findMany(cid, offset, limit);
	}

	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
	@Override
	public void delete(Long id) throws ServiceException {
		mapper.delete(id);
	}
}