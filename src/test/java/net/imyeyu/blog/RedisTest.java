package net.imyeyu.blog;

import net.imyeyu.blog.util.Redis;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.Duration;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class RedisTest {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Test
	public void ping() {
		Redis<String, String> ru = new Redis<>(redisTemplate, Redis.STRING_SERIALIZER);
		ru.set("key1中文", "value1", Duration.ofSeconds(60));
		System.out.println(ru.keys("*"));
		System.out.println(ru.get("key1中文"));
	}
}
