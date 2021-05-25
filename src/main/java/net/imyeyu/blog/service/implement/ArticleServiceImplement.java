package net.imyeyu.blog.service.implement;

import net.imyeyu.blog.entity.Article;
import net.imyeyu.blog.entity.ArticleHot;
import net.imyeyu.blog.mapper.ArticleMapper;
import net.imyeyu.blog.service.ArticleService;
import net.imyeyu.blog.util.Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文章操作
 * 
 * 夜雨 创建于 2021/2/17 17:48
 */
@Service
public class ArticleServiceImplement implements ArticleService {

	@Autowired
	private RedisTemplate<Long, ArticleHot> redisArticleHot;

	@Autowired
	private RedisTemplate<String, Long> redisArticleRead;

	@Autowired
	private ArticleMapper mapper;

	@Override
	public void create(Article t) {
	}

	@Override
	public Article find(Long id) {
		return mapper.findById(id);
	}

	@Override
	public List<Article> find(long offset, int limit) {
		return mapper.find(offset, limit);
	}

	@Override
	public List<Article> findByList(long offset, int limit) {
		return find(offset, limit).stream().filter(article -> !article.isHide()).collect(Collectors.toList());
	}

	@Override
	public void update(Article article) {
		mapper.update(article);
	}

	@Override
	public Long delete(Long... ids) {
		return null;
	}

	@Override
	public List<ArticleHot> getArticleHot() {
		Redis<Long, ArticleHot> redis = new Redis<>(redisArticleHot);
		try {
			List<ArticleHot> acs = redis.values();
			acs.sort(Comparator.comparing(ArticleHot::getCount).reversed());
			return acs.subList(0, Math.min(10, acs.size()));
		} catch (Exception e) {
			redis.flushAll();
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@Override
	public void comment(Article article) {
		article.comment();
		update(article);
	}

	@Override
	public void read(String ip, Article article) {
		Redis<String, Long> rdRead = new Redis<>(redisArticleRead);

		if (!rdRead.contains(ip, article.getId()) && !article.isHide()) {
			// 3 小时内访问记录
			rdRead.add(ip, article.getId());
			rdRead.expire(ip, 3);
			article.read();
			update(article);

			// 每周访问计数
			Redis<Long, ArticleHot> rdHot = new Redis<>(redisArticleHot);
			ArticleHot ac = rdHot.get(article.getId());
			if (ac == null) {
				ac = new ArticleHot(article.getId(), article.getTitle());
				ac.setRecentAt(System.currentTimeMillis());
				rdHot.set(article.getId(), ac, Duration.ofDays(7));
			} else {
				ac.increment();
				ac.setRecentAt(System.currentTimeMillis());
				rdHot.set(article.getId(), ac, true);
			}
		}
	}
}