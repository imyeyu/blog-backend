package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.Comment;
import net.imyeyu.blogapi.entity.CommentReply;
import net.imyeyu.blogapi.mapper.CommentMapper;
import net.imyeyu.blogapi.service.ArticleService;
import net.imyeyu.blogapi.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论操作服务实现
 * 
 * <p>夜雨 创建于 2021-02-23 21:41
 */
@Service
public class CommentServiceImplement implements CommentService {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private CommentMapper mapper;

	@Override
	public void create(Comment comment) throws ServiceException {
		if (articleService.find(comment.getArticleId()).isCanComment()) {
			comment.setCreatedAt(System.currentTimeMillis());
			mapper.create(comment);
		} else {
			throw new ServiceException(ReturnCode.REQUEST_BAD, "该文章已关闭评论");
		}
	}

	@Override
	public List<Comment> findMany(Long articleId, Long offset) {
		return mapper.findMany(articleId, offset);
	}

	// 子评论
	@Override
	public void createReply(CommentReply commentReply) {
		commentReply.setCreatedAt(System.currentTimeMillis());
		mapper.createReply(commentReply);
	}

	@Override
	public List<CommentReply> findManyReplies(Long commentId, Long offset) {
		return mapper.findManyReplies(commentId, offset);
	}

	@Override
	public int getLength(Long articleId) {
		return mapper.getLength(articleId);
	}
}