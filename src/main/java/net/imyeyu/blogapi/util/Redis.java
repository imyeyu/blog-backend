package net.imyeyu.blogapi.util;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * RedisTemplate 功能封装，简化 Redis 操作
 * <p>serializer 为该 RedisTemplate 的键的序列化操作，序列化解析器由 {@link net.imyeyu.blogapi.config.RedisConfig} 提供
 *
 * <p>夜雨 创建于 2021-03-02 17:46
 */
public record Redis<K, T>(RedisTemplate<K, T> redis, RedisSerializer<K> serializer) {

	/**
	 * 设置存活时间
	 *
	 * @param key     键
	 * @param timeout 存活时长
	 */
	public void expire(K key, Duration timeout) {
		redis.expire(key, timeout);
	}

	/**
	 * 设置存活时间
	 *
	 * @param key 键
	 * @param s   存活秒数
	 */
	public void expire(K key, long s) {
		expire(key, Duration.ofSeconds(s));
	}

	/**
	 * 设置存活时间
	 *
	 * @param key 键
	 * @param h   存活小时
	 */
	public void expire(K key, int h) {
		expire(key, Duration.ofHours(h));
	}

	/**
	 * 设置数据，生存时间为永久
	 *
	 * @param key 键
	 * @param v   值
	 */
	public void set(K key, T v) {
		redis.opsForValue().set(key, v);
	}

	/**
	 * 设置数据
	 *
	 * @param key         键
	 * @param v           值
	 * @param keepTimeout 是否保持剩余生存时间
	 */
	public void set(K key, T v, boolean keepTimeout) {
		if (keepTimeout) {
			set(key, v, null);
		} else {
			set(key, v);
		}
	}

	/**
	 * 设置数据，会重置生存时间
	 *
	 * @param key 键
	 * @param v   值
	 * @param s   存活秒数
	 */
	public void set(K key, T v, long s) {
		set(key, v, Duration.ofSeconds(s));
	}

	/**
	 * 设置数据，会重置生存时间
	 *
	 * @param key 键
	 * @param v   值
	 * @param h   存活小时
	 */
	public void set(K key, T v, int h) {
		set(key, v, Duration.ofHours(h));
	}

	/**
	 * 设置数据
	 *
	 * @param key     键
	 * @param v       值
	 * @param timeout 存活时间（为 null 时保持剩余生存时间）
	 */
	public void set(K key, T v, Duration timeout) {
		if (timeout == null) {
			Long expire = redis.getExpire(key, TimeUnit.SECONDS);
			// 存在剩余生存时间并且没有死亡
			if (expire != null && 0 < expire) {
				redis.opsForValue().set(key, v, Duration.ofSeconds(expire));
			} else {
				redis.opsForValue().set(key, v);
			}
		} else {
			redis.opsForValue().set(key, v, timeout);
		}
	}

	/**
	 * 获取值
	 *
	 * @param key 键
	 * @return 值
	 */
	public T get(K key) {
		return redis.opsForValue().get(key);
	}

	/**
	 * 获取值，强转为 String
	 *
	 * @param key 键
	 * @return 值
	 */
	public String getString(K key) {
		return get(key).toString();
	}

	/**
	 * 获取值，强转为 Boolean
	 *
	 * @param key 键
	 * @return 值
	 */
	public Boolean is(K key) {
		return Boolean.parseBoolean(getString(key));
	}

	/**
	 * 获取值，强转为 Boolean 并取反
	 *
	 * @param key 键
	 * @return 值
	 */
	public Boolean not(K key) {
		return !is(key);
	}

	/**
	 * 是否存在
	 *
	 * @param key 键
	 * @return true 为存在
	 */
	public boolean has(K key) {
		return get(key) != null;
	}

	/**
	 * 对列表添加值
	 *
	 * @param key 键
	 * @param t   值
	 */
	public void add(K key, T t) {
		redis.opsForList().leftPush(key, t);
	}

	/**
	 * 对列表批量添加值
	 *
	 * @param key 键
	 * @param t   值
	 */
	@SafeVarargs
	public final void addAll(K key, T... t) {
		redis.opsForList().leftPushAll(key, t);
	}

	/**
	 * 获取为列表
	 *
	 * @param key 键
	 * @return 列表
	 */
	public List<T> getList(K key) {
		return redis.opsForList().range(key, 0, -1);
	}

	/**
	 * 值为列表时查找是否存在某值
	 *
	 * @param key 键
	 * @param t   值
	 * @return true 为存在
	 */
	public boolean contains(K key, T t) {
		return getList(key).contains(t);
	}

	/** @return 所有值 */
	public List<T> values() {
		List<T> r = new ArrayList<>();
		List<K> keys = keys("*");
		for (K key : keys) {
			r.add(get(key));
		}
		return r;
	}

	/**
	 * scan 实现
	 *
	 * @param pattern  表达式
	 * @param consumer 对迭代到的 key 进行操作
	 */
	public void scan(String pattern, Consumer<byte[]> consumer) {
		redis.execute((RedisConnection connection) -> {
			try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(Long.MAX_VALUE).match(pattern).build())) {
				cursor.forEachRemaining(consumer);
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		});
	}

	/**
	 * 获取符合条件的 key
	 *
	 * @param pattern 表达式
	 * @return keys
	 */
	public List<K> keys(String pattern) {
		List<K> keys = new ArrayList<>();
		scan(pattern, item -> {
			if (item != null) {
				keys.add(serializer.deserialize(item));
			}
		});
		return keys;
	}

	/**
	 * 销毁对象
	 *
	 * @param k 键
	 * @return true 为成功
	 */
	public boolean destroy(K k) {
		if (!ObjectUtils.isEmpty(k) && has(k)) {
			Boolean b = redis.delete(k);
			return b != null && b;
		}
		return false;
	}

	/** 删库 */
	public void flushAll() {
		Objects.requireNonNull(redis.getConnectionFactory()).getConnection().flushAll();
	}
}