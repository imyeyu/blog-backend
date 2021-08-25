package net.imyeyu.blogapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.imyeyu.blogapi.annotation.Entity;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.service.ArticleService;
import net.imyeyu.blogapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.List;

/**
 * 评论
 *
 * <p>夜雨 创建于 2021-02-25 14:46
 */
@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Comment extends BaseEntity implements Serializable {

	@Transient
	private transient static ArticleService articleService;

	@Transient
	private transient static UserService userService;

	private Long articleId;
	private Long userId;
	private String nick;
	private String data;

	private int repliesLength;
	private Article article;
	private User user;
	private List<CommentReply> replies;

	@Autowired
	public Comment(ArticleService articleService, UserService userService) {
		Comment.articleService = articleService;
		Comment.userService = userService;
	}

	/**
	 * 携带文章摘要
	 *
	 * @return 本实体
	 * @throws ServiceException 服务异常
	 */
	public Comment withSimpleArticle() throws ServiceException {
		article = articleService.findSimple(articleId);
		return this;
	}

	/**
	 * 携带评论用户
	 *
	 * @return 本实体
	 * @throws ServiceException 服务异常
	 */
	public Comment withUser() throws ServiceException {
		user = userService.find(userId).withData();
		return this;
	}
}