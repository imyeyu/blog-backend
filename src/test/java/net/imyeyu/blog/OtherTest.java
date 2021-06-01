package net.imyeyu.blog;

import org.junit.Test;

import java.security.SecureRandom;

/**
 * 其他测试
 *
 * 夜雨 创建于 2021-05-25 19:54
 */
public class OtherTest {

	@Test
	public void unixTime() {
		System.out.println(System.currentTimeMillis());
	}

	@Test
	public void secureRandom() {
		for (int i = 0; i < 10; i++) {
			System.out.println(new SecureRandom().nextLong());
		}
	}
}
