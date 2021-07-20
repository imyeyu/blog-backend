package net.imyeyu.blogapi.controller;

import net.imyeyu.blogapi.bean.CaptchaData;
import net.imyeyu.blogapi.bean.Response;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.Comment;
import net.imyeyu.blogapi.entity.CommentReply;
import net.imyeyu.blogapi.service.ArticleService;
import net.imyeyu.blogapi.service.CommentService;
import net.imyeyu.blogapi.util.Captcha;
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
	private CommentService commentService;

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
	 * 提交评论
	 *
	 * @param cd 含验证评论
	 * @return 评论结果
	 */
	@PostMapping("")
	public Response<?> create(@RequestBody CaptchaData<Comment> cd) {
		// 验证码
		try {
			Captcha.test(cd.getCaptcha(), "COMMENT");
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		}
		try {
			commentService.create(cd.getData());
			return new Response<>(ReturnCode.SUCCESS, cd.getData());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, "服务端异常：" + e);
		}
	}

	/**
	 * 提交回复
	 *
	 * @param cd 含验证评论回复
	 * @return 回复结果
	 */
	@PostMapping("/reply")
	public Response<?> createReply(@RequestBody CaptchaData<CommentReply> cd) {
		// 验证码
		try {
			Captcha.test(cd.getCaptcha(), "COMMENT_REPLY");
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		}
		try {
			commentService.createReply(cd.getData());
			return new Response<>(ReturnCode.SUCCESS, cd.getData());
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, "服务端异常：" + e);
		}
	}
}