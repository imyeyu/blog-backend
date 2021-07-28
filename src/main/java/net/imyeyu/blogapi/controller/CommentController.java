package net.imyeyu.blogapi.controller;

import net.imyeyu.blogapi.annotation.AOPLog;
import net.imyeyu.blogapi.bean.CaptchaData;
import net.imyeyu.blogapi.bean.Response;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.bean.SettingKey;
import net.imyeyu.blogapi.bean.TokenData;
import net.imyeyu.blogapi.entity.Comment;
import net.imyeyu.blogapi.entity.CommentReply;
import net.imyeyu.blogapi.service.ArticleService;
import net.imyeyu.blogapi.service.CommentService;
import net.imyeyu.blogapi.service.SettingService;
import net.imyeyu.blogapi.service.UserService;
import net.imyeyu.blogapi.util.Captcha;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	private SettingService settingService;

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

	@RequestMapping("/reply")
	public Response<?> getRepliesByCommentId(Long commentId, Long offset) {
		return new Response<>(ReturnCode.SUCCESS, commentService.findManyReplies(commentId, offset));
	}

	/**
	 * 提交评论，允许游客操作，令牌不是必要参数
	 *
	 * @param td 令牌通信对象（可能有登录用户的令牌）
	 * @return 评论结果
	 */
	@AOPLog
	@PostMapping("")
	public Response<?> create(@RequestBody TokenData<CaptchaData<Comment>> td) {
		try {
			// 功能状态
			if (settingService.not(SettingKey.ENABLE_COMMENT)) {
				return new Response<>(ReturnCode.ERROR_OFF_SERVICE, "评论服务未启用");
			}
			Comment comment = td.getData().getData();
			// 评论数据
			if (ObjectUtils.isEmpty(comment)) {
				return new Response<>(ReturnCode.PARAMS_BAD, "无效的请求");
			}
			// 令牌和账号验证
			if (!StringUtils.isEmpty(td.getToken())) {
				if (!userService.isSignedIn(td.getToken())) {
					return new Response<>(ReturnCode.REQUEST_BAD, "无效的登录令牌，请重新登录");
				}
				if (userService.find(comment.getUserId()).isMuting()) {
					return new Response<>(ReturnCode.REQUEST_BAD, "该账号被禁止评论");
				}
			}
			// 昵称
			if (StringUtils.isEmpty(comment.getNick().trim())) {
				return new Response<>(ReturnCode.PARAMS_MISS, "请输入昵称");
			}
			// 内容
			if (StringUtils.isEmpty(comment.getData().trim())) {
				return new Response<>(ReturnCode.PARAMS_MISS, "请输入评论内容");
			}
			// 验证码
			try {
				Captcha.test(td.getData().getCaptcha(), "COMMENT");
			} catch (ServiceException e) {
				return new Response<>(e.getCode(), e);
			}
			commentService.create(comment);
			return new Response<>(ReturnCode.SUCCESS, comment);
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
	 * @param td 令牌通信对象（可能有登录用户的令牌）
	 * @return 回复结果
	 */
	@AOPLog
	@PostMapping("/reply")
	public Response<?> createReply(@RequestBody TokenData<CaptchaData<CommentReply>> td) {
		try {
			// 功能状态
			if (settingService.not(SettingKey.ENABLE_COMMENT)) {
				return new Response<>(ReturnCode.ERROR_OFF_SERVICE, "评论服务未启用");
			}
			CommentReply cr = td.getData().getData();
			if (!StringUtils.isEmpty(td.getToken()) && !userService.isSignedIn(td.getToken())) {
				return new Response<>(ReturnCode.REQUEST_BAD, "无效的登录令牌，请重新登录");
			}
			// 回复数据
			if (ObjectUtils.isEmpty(cr) || ObjectUtils.isEmpty(cr.getCommentId())) {
				return new Response<>(ReturnCode.PARAMS_BAD, "无效的请求");
			}
			// 昵称
			if (StringUtils.isEmpty(cr.getSenderNick().trim())) {
				return new Response<>(ReturnCode.PARAMS_MISS, "请输入昵称");
			}
			// 内容
			if (StringUtils.isEmpty(cr.getData().trim())) {
				return new Response<>(ReturnCode.PARAMS_MISS, "请输入回复内容");
			}
			// 验证码
			try {
				Captcha.test(td.getData().getCaptcha(), "COMMENT_REPLY");
			} catch (ServiceException e) {
				return new Response<>(e.getCode(), e);
			}
			commentService.createReply(cr);
			return new Response<>(ReturnCode.SUCCESS, cr);
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, "服务异常：" + e);
		}
	}
}