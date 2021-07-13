package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.ArticleClass;
import net.imyeyu.blogapi.mapper.ArticleClassMapper;
import net.imyeyu.blogapi.service.ArticleClassService;
import net.imyeyu.blogapi.vo.ArticleClassSide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>夜雨 创建于 2021-07-04 19:29
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
	public ArticleClassSide findBySide() {

		return null;
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