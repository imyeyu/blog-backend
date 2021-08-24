package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.CommentReply;
import net.imyeyu.blogapi.mapper.CommentReplyMapper;
import net.imyeyu.blogapi.service.CommentReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论回复服务实现
 *
 * <p>夜雨 创建于 2021-08-24 10:34
 */
@Service
public class CommentReplyServiceImplement implements CommentReplyService {

	@Autowired
	private CommentReplyMapper mapper;

	@Override
	public void deleteByUID(Long uid) throws ServiceException {
		mapper.deleteByUID(uid);
	}

	@Override
	public void deleteByCID(Long cid) throws ServiceException {
		mapper.deleteByCID(cid);
	}

	@Override
	public void create(CommentReply commentReply) throws ServiceException {
		commentReply.setCreatedAt(System.currentTimeMillis());
		mapper.create(commentReply);
	}

	@Override
	public CommentReply find(Long id) throws ServiceException {
		return mapper.find(id);
	}

	@Override
	public List<CommentReply> findMany(Long cid, Long offset, int limit) throws ServiceException {
		return mapper.findMany(cid, offset, limit);
	}

	@Override
	public void delete(Long id) throws ServiceException {
		mapper.delete(id);
	}
}