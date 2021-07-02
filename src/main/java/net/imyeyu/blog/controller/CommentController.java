package net.imyeyu.blog.controller;

import net.imyeyu.blog.bean.CaptchaData;
import net.imyeyu.blog.bean.Response;
import net.imyeyu.blog.bean.ReturnCode;
import net.imyeyu.blog.entity.Comment;
import net.imyeyu.blog.entity.CommentReply;
import net.imyeyu.blog.service.ArticleService;
import net.imyeyu.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 评论操作接口
 *
 * 夜雨 创建于 2021/2/23 21:36
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
	public Response<?> findByArticleId(Long articleId, long offset) {
		return new Response<>(ReturnCode.SUCCESS, commentService.findByArticleId(articleId, offset));
	}

	@RequestMapping("/reply")
	public Response<?> findRepliesByCommentId(Long commentId, Long offset) {
		return new Response<>(ReturnCode.SUCCESS, commentService.findRepliesByCommentId(commentId, offset));
	}

	/**
	 * 提交评论
	 *
	 * @param cd 含验证评论
	 * @return 评论结果
	 */
	@PostMapping("")
	public Response<?> create(@RequestBody CaptchaData<Comment> cd) {
		try {
			articleService.comment(articleService.find(cd.getData().getArticleId()));
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
	 * @param commentReply 回复对象
	 * @return 回复结果
	 */
	@PostMapping("/reply")
	public Response<?> createReply(@RequestBody CommentReply commentReply) {
		try {
			long aid = commentService.find(commentReply.getCommentId()).getArticleId();
			articleService.comment(articleService.find(aid));
			commentService.createReply(commentReply);
			return new Response<>(ReturnCode.SUCCESS, commentReply);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, "服务端异常：" + e);
		}
	}
}