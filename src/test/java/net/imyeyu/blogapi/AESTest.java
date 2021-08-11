package net.imyeyu.blogapi;

import net.imyeyu.betterjava.Encode;
import net.imyeyu.blogapi.util.AES;
import org.junit.Test;

/**
 * AES 测试
 *
 * <p>夜雨 创建于 2021-08-11 09:39
 */
public class AESTest {

	@Test
	public void generate() throws Exception {
		AES aes = new AES();
		byte[] key = aes.initKey();
		System.out.println(Encode.toHex(key));
	}

	@Test
	public void test() throws Exception {
		AES aes = new AES();
		String s = "hello world";
		System.out.println("string = " + s);
		byte[] key = aes.initKey();
		String hex = Encode.toHex(key);
		System.out.println("hexKey = " + hex);
		byte[] endata = aes.encrypt(s.getBytes(), key);
		System.out.println("endata = " + Encode.toHex(endata));
		key = Encode.fromHex(hex);
		byte[] dedata = aes.decrypt(endata, key);
		System.out.println("dedata = " + new String(dedata));
	}
}