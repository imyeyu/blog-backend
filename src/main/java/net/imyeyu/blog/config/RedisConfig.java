package net.imyeyu.blog.config;

import net.imyeyu.blog.entity.ArticleHot;
import net.imyeyu.blog.util.Redis;
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

import java.time.Duration;

/**
 * Redis 配置
 *
 * 夜雨 创建于 2021/2/23 21:36
 */
@Configuration
@EnableAutoConfiguration
public class RedisConfig extends CachingConfigurerSupport {

	// 数据库
	@Value("${spring.redis.database.article-hot}")
	private int articleHotDB;

	@Value("${spring.redis.database.article-read}")
	private int articleReadDB;

	@Value("${spring.redis.database.user-token}")
	private int userTokenDB;

	// 连接配置
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
	 * <p>文章访问统计
	 * <p>键为文章 ID，值为文章热度对象
	 *
	 * @return RedisTemplate
	 */
	@Bean("redisArticleHot")
	public RedisTemplate<Long, ArticleHot> getArticleHotRedisTemplate() {
		return getRedisTemplate(articleHotDB, Redis.LONG_SERIALIZER);
	}

	/**
	 * <p>文章访问记录
	 * <p>键为用户 IP，值为已访问文章 ID
	 *
	 * @return RedisTemplate
	 */
	@Bean("redisArticleRead")
	public RedisTemplate<String, Long> getArticleReadRedisTemplate() {
		return getRedisTemplate(articleReadDB, Redis.STRING_SERIALIZER);
	}

	/**
	 * <p>用户登录令牌缓存
	 * <p>键为用户 ID，值为令牌
	 *
	 * @return RedisTemplate
	 */
	@Bean("redisUserToken")
	public RedisTemplate<Long, String> getUserTokenRedisTemplate() {
		return getRedisTemplate(userTokenDB, Redis.LONG_SERIALIZER);
	}

	/**
	 * <p>构造自定义 RedisTemplate
	 * <p>针对同一服务器不同数据库
	 *
	 * @param database   数据库
	 * @param serializer 序列化方式
	 * @param <K> 键类型
	 * @param <T> 值类型
	 * @return RedisTemplate
	 */
	private <K, T> RedisTemplate<K, T> getRedisTemplate(int database, RedisSerializer<K> serializer) {
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

		RedisTemplate<K, T> rt = new RedisTemplate<>();
		rt.setConnectionFactory(factory);
		rt.setKeySerializer(serializer);
		rt.setHashKeySerializer(serializer);
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