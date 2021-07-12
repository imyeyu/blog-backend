package net.imyeyu.blog.service.implement;

import net.imyeyu.blog.bean.ReturnCode;
import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.Article;
import net.imyeyu.blog.entity.ArticleHot;
import net.imyeyu.blog.entity.ArticleLabel;
import net.imyeyu.blog.mapper.ArticleMapper;
import net.imyeyu.blog.service.ArticleService;
import net.imyeyu.blog.util.Redis;
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

	@Transactional
	@Override
	public void create(Article article) throws ServiceException {
		mapper.create(article);
		syncLabels(article);
	}

	@Override
	public Article find(Long id) throws ServiceException {
		return mapper.find(id);
	}

	@Override
	public List<Article> findMany(long offset, int limit) throws ServiceException {
		return mapper.findMany(offset, limit);
	}

	@Override
	public List<Article> findManyByList(long offset, int limit) {
		return mapper.findManyByList(offset, limit);
	}

	@Override
	public void update(Article article) {
		mapper.update(article);
		syncLabels(article);
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
			ArticleHot ac = redisArticleHot.get(article.getId());
			if (ac == null) {
				ac = new ArticleHot(article.getId(), article.getTitle(), article.getType());
				ac.setRecentAt(System.currentTimeMillis());
				redisArticleHot.set(article.getId(), ac, Duration.ofDays(7));
			} else {
				ac.increment();
				ac.setRecentAt(System.currentTimeMillis());
				redisArticleHot.set(article.getId(), ac, true);
			}
		}
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