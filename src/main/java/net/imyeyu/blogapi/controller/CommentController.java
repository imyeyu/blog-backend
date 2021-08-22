package net.imyeyu.blogapi.controller;

import net.imyeyu.blogapi.annotation.AOPLog;
import net.imyeyu.blogapi.annotation.QPSLimit;
import net.imyeyu.blogapi.bean.CaptchaData;
import net.imyeyu.blogapi.bean.Response;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.bean.SettingsKey;
import net.imyeyu.blogapi.entity.Comment;
import net.imyeyu.blogapi.entity.CommentReply;
import net.imyeyu.blogapi.service.ArticleService;
import net.imyeyu.blogapi.service.CommentService;
import net.imyeyu.blogapi.service.SettingsService;
import net.imyeyu.blogapi.service.UserService;
import net.imyeyu.blogapi.util.Captcha;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 评论操作接口
 *
 * <p>夜雨 创建于 2021-02-23 21:36
 */
@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private SettingsService settingsService;

	/**
	 * 获取评论
	 *
	 * @param articleId 文章 ID
	 * @param offset    偏移
	 * @return 文章评论
	 */
	@RequestMapping("")
	public Response<?> getByArticleId(Long articleId, long offset) {
		return new Response<>(ReturnCode.SUCCESS, commentService.findMany(articleId, offset));
	}

	/**
	 * 获取子评论
	 *
	 * @param commentId 评论 ID
	 * @param offset    偏移
	 * @return 子评论
	 */
	@QPSLimit
	@RequestMapping("/reply")
	public Response<?> getRepliesByCommentId(Long commentId, Long offset) {
		return new Response<>(ReturnCode.SUCCESS, commentService.findManyReplies(commentId, offset));
	}

	/**
	 * 提交评论，允许游客操作，令牌不是必要参数
	 *
	 * @param cd 验证码对象
	 * @return 评论结果
	 */
	@AOPLog
	@QPSLimit
	@PostMapping("")
	public Response<?> create(@RequestBody CaptchaData<Comment> cd, @RequestHeader("Token") String token) {
		try {
			// 功能状态
			if (settingsService.not(SettingsKey.ENABLE_COMMENT)) {
				return new Response<>(ReturnCode.ERROR_OFF_SERVICE, "评论服务未启用");
			}
			Comment comment = cd.getData();
			// 评论数据
			if (ObjectUtils.isEmpty(comment)) {
				return new Response<>(ReturnCode.PARAMS_BAD, "无效的请求");
			}
			// 令牌和账号验证
			boolean isSignedIn = false;
			if (!StringUtils.isEmpty(token)) {
				if (!userService.isSignedIn(token)) {
					return new Response<>(ReturnCode.REQUEST_BAD, "无效的登录令牌，请重新登录");
				}
				if (userService.find(comment.getUserId()).isMuting()) {
					return new Response<>(ReturnCode.REQUEST_BAD, "该账号被禁止评论");
				}
				isSignedIn = true;
			} else {
				// 昵称
				if (StringUtils.isEmpty(comment.getNick().trim())) {
					return new Response<>(ReturnCode.PARAMS_MISS, "请输入昵称");
				}
			}
			// 内容
			if (StringUtils.isEmpty(comment.getData().trim())) {
				return new Response<>(ReturnCode.PARAMS_MISS, "请输入评论内容");
			}
			// 验证码
			try {
				Captcha.test(cd.getCaptcha(), "COMMENT");
			} catch (ServiceException e) {
				return new Response<>(e.getCode(), e);
			}
			commentService.create(comment);
			if (isSignedIn) {
				return new Response<>(ReturnCode.SUCCESS, comment.withUser());
			} else {
				return new Response<>(ReturnCode.SUCCESS, comment);
			}
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, "服务异常：" + e);
		}
	}

	/**
	 * 提交回复，允许游客操作，令牌不是必要参数
	 *
	 * @param cd 验证码对象
	 * @return 回复结果
	 */
	@AOPLog
	@QPSLimit
	@PostMapping("/reply")
	public Response<?> createReply(@RequestBody CaptchaData<CommentReply> cd, @RequestHeader("Token") String token) {
		try {
			// 功能状态
			if (settingsService.not(SettingsKey.ENABLE_COMMENT)) {
				return new Response<>(ReturnCode.ERROR_OFF_SERVICE, "评论服务未启用");
			}
			CommentReply commentReply = cd.getData();
			// 令牌和账号验证
			boolean isSignedIn = false;
			if (!StringUtils.isEmpty(token)) {
				if (!userService.isSignedIn(token)) {
					return new Response<>(ReturnCode.REQUEST_BAD, "无效的登录令牌，请重新登录");
				}
				if (userService.find(commentReply.getSenderId()).isMuting()) {
					return new Response<>(ReturnCode.REQUEST_BAD, "该账号被禁止评论");
				}
				isSignedIn = true;
			} else {
				// 昵称
				if (StringUtils.isEmpty(commentReply.getSenderNick().trim())) {
					return new Response<>(ReturnCode.PARAMS_MISS, "请输入昵称");
				}
			}
			// 回复数据
			if (ObjectUtils.isEmpty(commentReply) || ObjectUtils.isEmpty(commentReply.getCommentId())) {
				return new Response<>(ReturnCode.PARAMS_BAD, "无效的请求");
			}
			// 内容
			if (StringUtils.isEmpty(commentReply.getData().trim())) {
				return new Response<>(ReturnCode.PARAMS_MISS, "请输入回复内容");
			}
			// 验证码
			try {
				Captcha.test(cd.getCaptcha(), "COMMENT_REPLY");
			} catch (ServiceException e) {
				return new Response<>(e.getCode(), e);
			}
			commentService.createReply(commentReply);
			if (isSignedIn) {
				if (ObjectUtils.isEmpty(commentReply.getReceiverId())) {
					return new Response<>(ReturnCode.SUCCESS, commentReply.withSender());
				} else {
					return new Response<>(ReturnCode.SUCCESS, commentReply.withSender().withReceiver());
				}
			} else {
				return new Response<>(ReturnCode.SUCCESS, commentReply);
			}
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, "服务异常：" + e);
		}
	}
}