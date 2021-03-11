package net.imyeyu.blog.controller;

import net.imyeyu.blog.entity.CaptchaData;
import net.imyeyu.blog.entity.Comment;
import net.imyeyu.blog.entity.CommentReply;
import net.imyeyu.blog.service.ArticleService;
import net.imyeyu.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
	public List<Comment> findByArticleId(Long articleId, long offset) {
		try {
			return commentService.findByArticleId(articleId, offset);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping("/reply")
	public List<CommentReply> findRepliesByCommentId(Long commentId, Long offset) {
		return commentService.findRepliesByCommentId(commentId, offset);
	}

	/**
	 * 提交评论
	 *
	 * @param cd 含验证评论
	 * @return 评论结果
	 */
	@PostMapping("")
	public ResponseEntity<?> create(@RequestBody CaptchaData<Comment> cd) {
		try {
			articleService.comment(articleService.findById(cd.getT().getArticleId()));
			commentService.create(cd.getT());
			return ResponseEntity.status(200).body(cd.getT());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}

	/**
	 * 提交回复
	 *
	 * @param commentReply 回复对象
	 * @return 回复结果
	 */
	@PostMapping("/reply")
	public ResponseEntity<?> createReply(@RequestBody CommentReply commentReply) {
		try {
			long aid = commentService.findById(commentReply.getCommentId()).getArticleId();
			articleService.comment(articleService.findById(aid));
			commentService.createReply(commentReply);
			return ResponseEntity.status(200).body(commentReply);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(e.getMessage());
		}
	}
}