package net.imyeyu.blogapi;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.service.FriendChainService;
import net.imyeyu.blogapi.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringTest {

	@Autowired
	private UserService service;

	@Test
	public void test() throws ServiceException {
		System.out.println(service.find(3L));
	}
}