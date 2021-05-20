package net.imyeyu.blog.service.implement;

import net.imyeyu.blog.entity.Article;
import net.imyeyu.blog.entity.ArticleHot;
import net.imyeyu.blog.mapper.ArticleMapper;
import net.imyeyu.blog.service.ArticleService;
import net.imyeyu.blog.util.RedisUtil;
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
	private RedisTemplate<String, ArticleHot> redisArticleHot;

	@Autowired
	private RedisTemplate<String, Long> redisArticleRead;

	@Autowired
	private ArticleMapper mapper;

	@Override
	public void create(Article t) {
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
	public Article findById(Long id) {
		return mapper.findById(id);
	}

	@Override
	public void update(Article article) {
		mapper.update(article);
	}

	@Override
	public Long delete(Long... ids) {
		return null;
	}

	public List<ArticleHot> getArticleHot() {
		RedisUtil<ArticleHot> redisUtil = new RedisUtil<>(redisArticleHot);
		try {
			List<ArticleHot> acs = redisUtil.values();
			acs.sort(Comparator.comparing(ArticleHot::getCount).reversed());
			return acs.subList(0, Math.min(10, acs.size()));
		} catch (Exception e) {
			redisUtil.flushAll();
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
		RedisUtil<Long> rdRead = new RedisUtil<>(redisArticleRead);

		if (!rdRead.contains(ip, article.getId()) && !article.isHide()) {
			// 3 小时内访问记录
			rdRead.add(ip, article.getId());
			rdRead.expire(ip, 3);
			article.read();
			update(article);

			// 每周访问计数
			RedisUtil<ArticleHot> rdHot = new RedisUtil<>(redisArticleHot);
			ArticleHot ac = rdHot.get(String.valueOf(article.getId()));
			if (ac == null) {
				ac = new ArticleHot(article.getId(), article.getTitle());
				ac.setRecentAt(System.currentTimeMillis());
				rdHot.set(String.valueOf(article.getId()), ac, Duration.ofDays(7));
			} else {
				ac.increment();
				ac.setRecentAt(System.currentTimeMillis());
				rdHot.set(String.valueOf(article.getId()), ac, true);
			}
		}
	}
}