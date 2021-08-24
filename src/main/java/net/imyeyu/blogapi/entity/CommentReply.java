package net.imyeyu.blogapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.imyeyu.blogapi.annotation.Entity;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;

/**
 * 评论回复
 *
 * <p>夜雨 创建于 2021-03-01 17:11
 */
@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommentReply extends BaseEntity implements Serializable {

	@Transient
	private transient static UserService userService;

	private Long commentId;
	private Long senderId;
	private Long receiverId;
	private String senderNick;
	private String receiverNick;
	private String data;

	private User sender, receiver;

	@Autowired
	public CommentReply(UserService userService) {
		CommentReply.userService = userService;
	}

	/**
	 * 携带回复者数据
	 *
	 * @return 本实体
	 * @throws ServiceException 服务异常
	 */
	public CommentReply withSender() throws ServiceException {
		sender = userService.find(senderId).withData();
		return this;
	}

	/**
	 * 携带被回复者数据
	 *
	 * @return 本实体
	 * @throws ServiceException 服务异常
	 */
	public CommentReply withReceiver() throws ServiceException {
		receiver = userService.find(receiverId).withData();
		return this;
	}
}