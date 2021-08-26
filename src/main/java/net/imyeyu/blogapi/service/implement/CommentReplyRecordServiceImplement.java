package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.CommentReplyRecord;
import net.imyeyu.blogapi.entity.UserComment;
import net.imyeyu.blogapi.mapper.CommentReplyRecordMapper;
import net.imyeyu.blogapi.service.CommentReplyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论回复记录服务实现
 *
 * <p>夜雨 创建于 2021-08-25 14:35
 */
@Service
public class CommentReplyRecordServiceImplement implements CommentReplyRecordService {

	@Autowired
	private CommentReplyRecordMapper mapper;

	@Override
	public List<UserComment> findManyByUID(Long uid, Long offset, int limit) throws ServiceException {
		return mapper.findManyByUID(uid, offset, limit);
	}

	@Override
	public void deleteByRID(Long rid) throws ServiceException {
		mapper.deleteByRID(rid);
	}

	@Override
	public void deleteByRIDwithUID(Long uid, Long rid) throws ServiceException {
		mapper.deleteByRIDwithUID(uid, rid);
	}

	@Override
	public void create(CommentReplyRecord commentReplyRecord) throws ServiceException {
		mapper.create(commentReplyRecord);
	}
}