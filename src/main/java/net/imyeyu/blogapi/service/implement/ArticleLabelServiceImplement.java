package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.Article;
import net.imyeyu.blogapi.entity.ArticleLabel;
import net.imyeyu.blogapi.mapper.ArticleLabelMapper;
import net.imyeyu.blogapi.service.ArticleLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public ArticleLabel findByName(String name) {
		return mapper.findByName(name);
	}

	@Override
	public List<ArticleLabel> findMany(long offset, int limit) throws ServiceException {
		return mapper.findMany(offset, limit);
	}

	@Override
	public List<ArticleLabel> findManyByArticleId(long aid) {
		return mapper.findManyByArticleId(aid);
	}

	@Override
	public void update(ArticleLabel articleLabel) {
		mapper.update(articleLabel);
	}

	@Override
	public Long delete(Long... ids) throws ServiceException {
		return mapper.delete(ids);
	}

	@Transactional
	@Override
	public void syncLabels(Article article) {
		mapper.clearLabels(article.getId());
		List<ArticleLabel> labels = article.getLabels();
		for (int i = 0; i < labels.size(); i++) {
			if (labels.get(i) != null) {
				mapper.addLabel(article.getId(), labels.get(i).getId());
			}
		}
	}
}