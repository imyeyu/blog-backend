package net.imyeyu.blog.service.implement;

import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.ArticleLabel;
import net.imyeyu.blog.mapper.ArticleLabelMapper;
import net.imyeyu.blog.service.ArticleLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>夜雨 创建于 2021-07-04 19:35
 */
@Service
public class ArticleLabelServiceImplement implements ArticleLabelService {

	@Autowired
	private ArticleLabelMapper mapper;

	@Override
	public void create(ArticleLabel articleLabel) throws ServiceException {
		mapper.create(articleLabel);
	}

	@Override
	public ArticleLabel find(Long id) throws ServiceException {
		return mapper.find(id);
	}

	@Override
	public List<ArticleLabel> findMany(long offset, int limit) throws ServiceException {
		return mapper.findMany(offset, limit);
	}

	@Override
	public ArticleLabel findByName(String name) {
		return mapper.findByName(name);
	}

	@Override
	public void update(ArticleLabel articleLabel) {
		mapper.update(articleLabel);
	}

	@Override
	public Long delete(Long... ids) throws ServiceException {
		return mapper.delete(ids);
	}
}