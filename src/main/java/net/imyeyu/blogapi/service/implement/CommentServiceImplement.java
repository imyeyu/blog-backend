package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.Comment;
import net.imyeyu.blogapi.mapper.CommentMapper;
import net.imyeyu.blogapi.service.ArticleService;
import net.imyeyu.blogapi.service.CommentReplyService;
import net.imyeyu.blogapi.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private CommentReplyService commentReplyService;

	@Autowired
	private CommentMapper mapper;

	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
	@Override
	public void deleteByUID(Long uid) throws ServiceException {
		List<Comment> comments = mapper.findAllByUID(uid);
		for (int i = 0; i < comments.size(); i++) {
			delete(comments.get(i).getId());
		}
		mapper.deleteByUID(uid);
	}

	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
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
	public List<Comment> findMany(Long aid, Long offset, int limit) {
		return mapper.findMany(aid, offset, limit);
	}

	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
	@Override
	public void delete(Long id) throws ServiceException {
		commentReplyService.deleteByCID(id);
		mapper.delete(id);
	}
}