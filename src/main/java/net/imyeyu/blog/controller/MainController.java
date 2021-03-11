package net.imyeyu.blog.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 主接口
 *
 * 夜雨 创建于 2021/2/23 21:38
 */
@RestController
public class MainController {
	
	@Autowired
	private RedisTemplate<String, String> redis;
	
	@RequestMapping("/")
	public String say() {
		return "It working";
	}
	
	@RequestMapping("set")
	public String set() {
		redis.opsForValue().set("redis", "Redis 缓存测试 + " + new SimpleDateFormat("H:mm:ss").format(new Date()));
		return "设置成功";
	}
	
	@RequestMapping("get")
	public String get() {
		return "缓存值" + redis.opsForValue().get("redis");
	}
	
	@RequestMapping("test")
	public String test() {
		return "ajax test";
	}
}