package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.Comment;
import net.imyeyu.blogapi.entity.CommentReply;
import net.imyeyu.blogapi.mapper.CommentMapper;
import net.imyeyu.blogapi.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论操作
 * 
 * <p>夜雨 创建于 2021-02-23 21:41
 */
@Service
public class CommentServiceImplement implements CommentService {

	@Autowired
	private CommentMapper mapper;

	@Override
	public void create(Comment comment) throws ServiceException {
		comment.setCreatedAt(System.currentTimeMillis());
		mapper.create(comment);
	}

	@Override
	public Comment find(Long id) throws ServiceException {
		Comment comment = mapper.find(id);
		if (comment != null) {
			return mapper.find(id);
		} else {
			throw new ServiceException(ReturnCode.RESULT_NULL, "找不到该评论");
		}
	}

	@Override
	public List<Comment> findMany(Long offset, int limit) {
		return null;
	}

	public List<Comment> findByArticleId(Long articleId, Long offset) {
		return mapper.findByArticleId(articleId, offset);
	}

	@Override
	public void update(Comment t) {
	}

	@Override
	public Long delete(Long... ids) {
		return null;
	}
	
	// 子评论
	public void createReply(CommentReply commentReply) {
		commentReply.setCreatedAt(System.currentTimeMillis());
		mapper.createReply(commentReply);
	}

	public List<CommentReply> findRepliesByCommentId(Long commentId, Long offset) {
		return mapper.findRepliesByCommentId(commentId, offset);
	}
}