package net.imyeyu.blog;

import net.imyeyu.blog.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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

	@Test
	public void testFindAll() {
	}

	@Test
	public void testFindById() {
	}
}