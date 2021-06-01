package net.imyeyu.blog.service.implement;

import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.Comment;
import net.imyeyu.blog.entity.CommentReply;
import net.imyeyu.blog.mapper.CommentMapper;
import net.imyeyu.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论操作
 * 
 * 夜雨 创建于 2021/2/23 21:41
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
	public Comment find(Long id) {
		return null;
	}

	@Override
	public List<Comment> findMany(long offset, int limit) {
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