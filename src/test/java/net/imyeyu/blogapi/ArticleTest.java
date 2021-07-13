package net.imyeyu.blogapi;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.service.ArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ArticleTest {

	@Autowired
	private ArticleService service;

	@Test
	public void test() throws ServiceException {
//		System.out.println(new Gson().toJson(service.find(1L)));
	}
}