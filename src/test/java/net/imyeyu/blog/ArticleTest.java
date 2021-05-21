package net.imyeyu.blog;

import net.imyeyu.blog.entity.Article;
import net.imyeyu.blog.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * 文章接口测试
 *
 * 夜雨 创建于 2021/2/17 18:03
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
class ArticleTest {

	@Autowired
	ArticleService service;

	@Test // 正常
	public void testFindAll() {
		List<Article> l = service.find(0, 16);
		l.stream().map(Article::getTitle).forEachOrdered(System.out::println);
	}

	@Test // 正常
	public void testFindById() {
		System.out.println(service.find(2L));
	}
}