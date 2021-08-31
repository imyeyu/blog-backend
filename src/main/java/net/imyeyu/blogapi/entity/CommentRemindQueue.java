package net.imyeyu.blogapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.imyeyu.blogapi.annotation.Entity;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.service.CommentReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;

/**
 * 评论回复提醒队列，和 CommentReplyRecord 不一样，本队列在推送消息后就删除，而后者会持久保存
 *
 * <p>基本逻辑：
 * <pre>
 *     触发：用户回复一条评论
 *     条件：被回复者是注册用户 && 不是回复自己 && 邮箱已验证 && 接收回复提醒邮件
 *     事件：添加本对象到队列列表，等待邮件推送服务调度，邮件推送服务
 * </pre>
 * 会针对用户收集本队列消息组合成邮件再一并推送
 *
 * <p>夜雨 创建于 2021-08-25 00:00
 */
@Data
@Entity
@NoArgsConstructor
public class CommentRemindQueue {

	@Transient
	private transient static CommentReplyService commentReplyService;

	private String UUID;
	private Long userId;
	private Long replyId;

	private CommentReply reply;

	@Autowired
	public CommentRemindQueue(CommentReplyService commentReplyService) {
		CommentRemindQueue.commentReplyService = commentReplyService;
	}

	/**
	 * 携带回复
	 *
	 * @return 本实体
	 * @throws ServiceException 服务异常
	 */
	public CommentRemindQueue withReply() throws ServiceException {
		reply = commentReplyService.find(replyId);
		return this;
	}
}