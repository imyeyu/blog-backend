package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.ArticleLabel;
import net.imyeyu.blogapi.mapper.ArticleLabelMapper;
import net.imyeyu.blogapi.service.ArticleLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章标签服务实现
 *
 * <p>夜雨 创建于 2021-07-04 19:35
 */
@Service
public class ArticleLabelServiceImplement implements ArticleLabelService {

	@Autowired
	private ArticleLabelMapper mapper;

	@Override
	public ArticleLabel find(Long id) throws ServiceException {
		ArticleLabel al = mapper.find(id);
		if (al != null) {
			return al;
		} else {
			throw new ServiceException(ReturnCode.RESULT_NULL, "找不到该标签");
		}
	}

	@Override
	public ArticleLabel findByName(String name) {
		return mapper.findByName(name);
	}

	@Override
	public List<ArticleLabel> findManyByArticleId(long aid) {
		return mapper.findManyByArticleId(aid);
	}
}