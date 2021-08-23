package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.Comment;
import net.imyeyu.blogapi.entity.CommentReply;
import net.imyeyu.blogapi.entity.UserComment;
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
	public void createReply(CommentReply commentReply) {
		commentReply.setCreatedAt(System.currentTimeMillis());
		mapper.createReply(commentReply);
	}

	@Override
	public CommentReply findReply(Long crid) {
		return mapper.findReply(crid);
	}

	@Override
	public List<CommentReply> findManyReplies(Long commentId, Long offset) {
		return mapper.findManyReplies(commentId, offset);
	}

	@Override
	public List<UserComment> findManyUserComment(Long uid, Long offset, int limit) {
		return mapper.findManyUserComment(uid, offset, limit);
	}

	@Override
	public List<UserComment> findManyUserCommentReplies(Long uid, Long offset, int limit) {
		return mapper.findManyUserCommentReplies(uid, offset, limit);
	}

	@Override
	public int getLength(Long articleId) {
		return mapper.getLength(articleId);
	}

	@Override
	public void deleteByUID(Long uid) throws ServiceException {
		List<Comment> comments = mapper.findAllByUID(uid);
		for (int i = 0; i < comments.size(); i++) {
			delete(comments.get(i).getId());
		}
		mapper.deleteByUID(uid);
	}

	@Override
	public void deleteReply(Long id) throws ServiceException {
		mapper.deleteReply(id);
	}

	@Override
	public void deleteReplyByUID(Long uid) throws ServiceException {
		mapper.deleteReplyByUID(uid);
	}

	@Override
	public void create(Comment comment) throws ServiceException {
		if (articleService.find(comment.getArticleId()).isCanComment()) {
			comment.setCreatedAt(System.currentTimeMillis());
			mapper.create(comment);
		} else {
			throw new ServiceException(ReturnCode.RESULT_BAN, "该文章已关闭评论");
		}
	}

	@Override
	public Comment find(Long id) throws ServiceException {
		return mapper.find(id);
	}

	@Override
	public List<Comment> findMany(Long articleId, Long offset) {
		return mapper.findMany(articleId, offset);
	}

	@Override
	public void delete(Long id) throws ServiceException {
		List<CommentReply> replies = mapper.findAllRepliesByCID(id);
		for (int i = 0; i < replies.size(); i++) {
			deleteReply(replies.get(i).getId());
		}
		mapper.delete(id);
	}
}