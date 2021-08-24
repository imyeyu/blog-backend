package net.imyeyu.blogapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.imyeyu.blogapi.annotation.Entity;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.service.CommentReplyService;
import net.imyeyu.blogapi.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;

/**
 * 评论举报
 *
 * <p>夜雨 创建于 2021-08-24 10:19
 */
@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CommentReport extends BaseEntity implements Serializable {

	@Transient
	private transient static CommentService commentService;

	@Transient
	private transient static CommentReplyService commentReplyService;

	private Long commentId;
	private Long commentReplyId;

	private String commentData;
	private String commentReplyData;
	private Comment comment;
	private CommentReply commentReply;

	@Autowired
	public CommentReport(CommentService commentService, CommentReplyService commentReplyService) {
		CommentReport.commentService = commentService;
		CommentReport.commentReplyService = commentReplyService;
	}

	/**
	 * 携带评论
	 *
	 * @return 本实体
	 * @throws ServiceException 服务异常
	 */
	public CommentReport withComment() throws ServiceException {
		comment = commentService.find(commentId);
		return this;
	}

	/**
	 * 携带回复
	 *
	 * @return 本实体
	 * @throws ServiceException 服务异常
	 */
	public CommentReport withCommentReply() throws ServiceException {
		commentReply = commentReplyService.find(commentReplyId);
		return this;
	}
}