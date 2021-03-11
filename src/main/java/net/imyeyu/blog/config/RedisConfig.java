package net.imyeyu.blog.config;

import net.imyeyu.blog.entity.ArticleHot;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis 配置
 *
 * 夜雨 创建于 2021/2/23 21:36
 */
@Configuration
@EnableAutoConfiguration
public class RedisConfig extends CachingConfigurerSupport {

	@Value("${spring.redis.database.article-hot}")
	private int articleHotDatabase;

	@Value("${spring.redis.database.article-read}")
	private int articleReadDatabase;

	@Value("${spring.redis.host}")
	private String host;

	@Value("${spring.redis.port}")
	private int port;

	@Value("${spring.redis.password}")
	private String password;

	@Value("${spring.redis.timeout}")
	private int timeout;

	@Value("${spring.redis.lettuce.pool.max-active}")
	private int maxActive;

	@Value("${spring.redis.lettuce.pool.min-idle}")
	private int minIdle;

	@Value("${spring.redis.lettuce.pool.max-idle}")
	private int maxIdle;

	@Value("${spring.redis.lettuce.pool.max-wait}")
	private int maxWait;

	/** @return Redis 连接池配置 */
	@Bean
	public GenericObjectPoolConfig<?> getPoolConfig() {
		GenericObjectPoolConfig<?> config = new GenericObjectPoolConfig<>();
		config.setMaxTotal(maxActive);
		config.setMinIdle(minIdle);
		config.setMaxIdle(maxIdle);
		config.setMaxWaitMillis(maxWait);
		return config;
	}

	/**
	 * 文章访问统计
	 *
	 * @return RedisTemplate
	 */
	@Bean("redisArticleHot")
	public RedisTemplate<String, ArticleHot> getArticleHotRedisTemplate() {
		return getRedisTemplate(articleHotDatabase);
	}

	/**
	 * 文章访问记录
	 *
	 * @return RedisTemplate
	 */
	@Bean("redisArticleRead")
	public RedisTemplate<String, Long> getArticleReadRedisTemplate() {
		return getRedisTemplate(articleReadDatabase);
	}

	/**
	 * 构造自定义 RedisTemplate
	 * 针对同一服务器不同数据库
	 *
	 * @param <T> 值类型
	 *
	 * @return RedisTemplate
	 */
	public <T> RedisTemplate<String, T> getRedisTemplate(int database) {
		// 构建 Redis 配置
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		// 连接参数
		config.setHostName(host);
		config.setPort(port);
		config.setPassword(RedisPassword.of(password));
		// 构造连接池
		LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder()
				.commandTimeout(Duration.ofMillis(timeout))
				.poolConfig(getPoolConfig())
				.build();
		// 重构连接工厂
		LettuceConnectionFactory factory = new LettuceConnectionFactory(config, clientConfig);
		// 设置数据库
		factory.setDatabase(database);
		factory.afterPropertiesSet();

		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		RedisTemplate<String, T> rt = new RedisTemplate<>();
		rt.setConnectionFactory(factory);
		rt.setKeySerializer(stringSerializer);
		rt.setHashKeySerializer(stringSerializer);
		rt.setConnectionFactory(factory);
		rt.afterPropertiesSet();
		return rt;
	}

	/**
	 * Redis key 生成策略
	 *
	 * @return KeyGenerator
	 */
	@Bean
	public KeyGenerator keyGenerator() {
		return (target, method, params) -> {
			StringBuilder sb = new StringBuilder();
			sb.append(target.getClass().getName());
			sb.append(method.getName());
			for (Object obj : params) {
				sb.append(obj.toString());
			}
			return sb.toString();
		};
	}
}