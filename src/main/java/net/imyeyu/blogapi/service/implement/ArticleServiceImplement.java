package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.Article;
import net.imyeyu.blogapi.entity.ArticleHot;
import net.imyeyu.blogapi.mapper.ArticleMapper;
import net.imyeyu.blogapi.service.ArticleLabelService;
import net.imyeyu.blogapi.service.ArticleService;
import net.imyeyu.blogapi.util.Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

/**
 * 文章操作
 * 
 * <p>夜雨 创建于 2021-02-17 17:48
 */
@Service
public class ArticleServiceImplement implements ArticleService {

	@Autowired
	private Redis<Long, ArticleHot> redisArticleHot;

	@Autowired
	private Redis<String, Long> redisArticleRead;

	@Autowired
	private ArticleMapper mapper;

	@Autowired
	private ArticleLabelService labelService;

	@Transactional
	@Override
	public void create(Article article) throws ServiceException {
		mapper.create(article);
		labelService.syncLabels(article);
	}

	@Override
	public Article find(Long id) throws ServiceException {
		Article article = mapper.find(id);
		article.setLabels(labelService.findManyByArticleId(id));
		return article;
	}

	@Override
	public List<Article> findMany(long offset, int limit) throws ServiceException {
		return mapper.findMany(offset, limit);
	}

	@Override
	public List<Article> findManyByList(long offset, int limit) {
		return mapper.findManyByList(offset, limit);
	}

	@Transactional
	@Override
	public void update(Article article) {
		mapper.update(article);
		labelService.syncLabels(article);
	}

	@Override
	public Long delete(Long... ids) {
		return null;
	}

	@Override
	public List<ArticleHot> getArticleHot() throws ServiceException {
		try {
			List<ArticleHot> acs = redisArticleHot.values();
			acs.sort(Comparator.comparing(ArticleHot::getCount).reversed());
			return acs.subList(0, Math.min(10, acs.size()));
		} catch (Exception e) {
			redisArticleHot.flushAll();
			e.printStackTrace();
			throw new ServiceException(ReturnCode.ERROR, e.getMessage());
		}
	}

	@Override
	public void comment(Article article) {
		article.comment();
		update(article);
	}

	@Override
	public void read(String ip, Article article) {
		if (!redisArticleRead.contains(ip, article.getId())) {
			// 3 小时内访问记录
			redisArticleRead.add(ip, article.getId());
			redisArticleRead.expire(ip, 3);
			article.read();
			update(article);

			// 每周访问计数
			if (article.getId() != 1) {
				ArticleHot ah = redisArticleHot.get(article.getId());
				if (ah == null) {
					ah = new ArticleHot(article.getId(), article.getTitle(), article.getType());
					ah.setRecentAt(System.currentTimeMillis());
					redisArticleHot.set(article.getId(), ah, Duration.ofDays(7));
				} else {
					ah.increment();
					ah.setRecentAt(System.currentTimeMillis());
					redisArticleHot.set(article.getId(), ah, true);
				}
			}
		}
	}
}