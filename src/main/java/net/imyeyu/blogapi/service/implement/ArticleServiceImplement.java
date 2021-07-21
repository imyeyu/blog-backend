package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.Article;
import net.imyeyu.blogapi.entity.ArticleClass;
import net.imyeyu.blogapi.entity.ArticleRanking;
import net.imyeyu.blogapi.entity.ArticleLabel;
import net.imyeyu.blogapi.mapper.ArticleMapper;
import net.imyeyu.blogapi.service.ArticleLabelService;
import net.imyeyu.blogapi.service.ArticleService;
import net.imyeyu.blogapi.service.CommentService;
import net.imyeyu.blogapi.util.Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

/**
 * 文章服务实现
 * 
 * <p>夜雨 创建于 2021-02-17 17:48
 */
@Service
public class ArticleServiceImplement implements ArticleService {

	@Autowired
	private Redis<Long, ArticleRanking> redisArticleRanking;

	@Autowired
	private Redis<String, Long> redisArticleRead;

	@Autowired
	private ArticleMapper mapper;

	@Autowired
	private CommentService commentService;

	@Autowired
	private ArticleLabelService labelService;

	@Override
	public Article find(Long id) throws ServiceException {
		Article article = mapper.find(id);
		if (article != null) {
			article.setLabels(labelService.findManyByArticleId(id));
			article.setComments(commentService.getLength(id));
			return article;
		} else {
			throw new ServiceException(ReturnCode.RESULT_NULL, "该文章不存在");
		}
	}

	@Override
	public List<Article> findMany(Long offset, int limit) throws ServiceException {
		return mapper.findMany(offset, limit);
	}

	@Override
	public List<Article> findManyByClass(ArticleClass clazz, Long offset, int limit) {
		return mapper.findManyByClass(clazz.getId(), offset, limit);
	}

	@Override
	public List<Article> findManyByLabel(ArticleLabel label, Long offset, int limit) {
		return mapper.findManyByLabel(label.getId(), offset, limit);
	}

	@Override
	public void update(Article article) throws ServiceException {
		mapper.update(article);
	}

	@Override
	public List<ArticleRanking> getRanking() throws ServiceException {
		try {
			List<ArticleRanking> acs = redisArticleRanking.values();
			acs.sort(Comparator.comparing(ArticleRanking::getCount).reversed());
			return acs.subList(0, Math.min(10, acs.size()));
		} catch (Exception e) {
			redisArticleRanking.flushAll();
			e.printStackTrace();
			throw new ServiceException(ReturnCode.ERROR, e.getMessage());
		}
	}

	@Override
	public void read(String ip, Article article) throws ServiceException {
		if (!redisArticleRead.contains(ip, article.getId())) {
			// 3 小时内访问记录
			redisArticleRead.add(ip, article.getId());
			redisArticleRead.expire(ip, 3);
			article.read();
			update(article);

			// 每周访问计数
			if (article.isCanRanking()) {
				ArticleRanking ah = redisArticleRanking.get(article.getId());
				if (ah == null) {
					ah = new ArticleRanking(article.getId(), article.getTitle(), article.getType());
					ah.setRecentAt(System.currentTimeMillis());
					redisArticleRanking.set(article.getId(), ah, Duration.ofDays(7));
				} else {
					ah.increment();
					ah.setRecentAt(System.currentTimeMillis());
					redisArticleRanking.set(article.getId(), ah, true);
				}
			}
		}
	}

	@Override
	public int like(Long aid) throws ServiceException {
		Article article = mapper.find(aid);
		article.like();
		update(article);
		return article.getLikes();
	}
}