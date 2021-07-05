package net.imyeyu.blog.service.implement;

import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.ArticleClass;
import net.imyeyu.blog.mapper.ArticleClassMapper;
import net.imyeyu.blog.service.ArticleClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 夜雨 创建于 2021-07-04 19:29
 */
@Service
public class ArticleClassServiceImplement implements ArticleClassService {

	@Autowired
	private ArticleClassMapper mapper;

	@Override
	public void create(ArticleClass articleClass) throws ServiceException {
		mapper.create(articleClass);
	}

	@Override
	public ArticleClass find(Long id) throws ServiceException {
		return mapper.find(id);
	}

	@Override
	public List<ArticleClass> findMany(long offset, int limit) throws ServiceException {
		return mapper.findMany(offset, limit);
	}

	@Override
	public void update(ArticleClass articleClass) {
		mapper.update(articleClass);
	}

	@Override
	public Long delete(Long... ids) throws ServiceException {
		return mapper.delete(ids);
	}
}