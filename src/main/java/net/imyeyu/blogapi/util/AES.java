package net.imyeyu.blogapi.util;

import net.imyeyu.betterjava.Encode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 加密，如果不传密钥，密钥从配置文件获取
 *
 * <p>夜雨 创建于 2021-08-11 00:52
 */
@Component
public class AES {

	@Value("${setting.aes-key-hex}")
	private String key;

	/**
	 * 生产密钥
	 *
	 * @return 密钥
	 * @throws Exception 生产异常
	 */
	public byte[] initKey() throws Exception {
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(256);
		return keyGen.generateKey().getEncoded();
	}

	/**
	 * 加密字符串
	 *
	 * @param data 待加密字符串
	 * @return 加密结果
	 * @throws Exception 加密异常
	 */
	public byte[] encrypt(String data) throws Exception {
		return encrypt(data.getBytes(), Encode.fromHex(key));
	}

	/**
	 * 加密字符串
	 *
	 * @param data 待加密字符串
	 * @param key  密钥
	 * @return 加密结果
	 * @throws Exception 加密异常
	 */
	public byte[] encrypt(String data, byte[] key) throws Exception {
		return encrypt(data.getBytes(), key);
	}

	/**
	 * 加密
	 *
	 * @param data 待加密字节数据
	 * @return 加密结果
	 * @throws Exception 加密异常
	 */
	public byte[] encrypt(byte[] data) throws Exception {
		return encrypt(data, Encode.fromHex(key));
	}

	/**
	 * 加密
	 *
	 * @param data 待加密字节数据
	 * @param key  密钥
	 * @return 加密结果
	 * @throws Exception 加密异常
	 */
	public byte[] encrypt(byte[] data, byte[] key) throws Exception {
		SecretKey secretKey = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey); // 密钥
		return cipher.doFinal(data); // 加密返回
	}

	/**
	 * 解密字符串
	 *
	 * @param data 待解密字符串
	 * @return 解密结果
	 * @throws Exception 解密异常
	 */
	public byte[] decrypt(String data) throws Exception {
		return decrypt(data.getBytes(), Encode.fromHex(key));
	}

	/**
	 * 解密字符串
	 *
	 * @param data 待解密字节数据
	 * @param key  密钥
	 * @return 解密结果
	 * @throws Exception 解密异常
	 */
	public byte[] decrypt(String data, byte[] key) throws Exception {
		return decrypt(data.getBytes(), key);
	}

	/**
	 * 解密
	 *
	 * @param data 待解密字节数据
	 * @return 解密结果
	 * @throws Exception 解密异常
	 */
	public byte[] decrypt(byte[] data) throws Exception {
		return decrypt(data, Encode.fromHex(key));
	}

	/**
	 * 解密
	 *
	 * @param data 待解密字节数据
	 * @param key  密钥
	 * @return 解密结果
	 * @throws Exception 解密异常
	 */
	public byte[] decrypt(byte[] data, byte[] key) throws Exception {
		SecretKey secretKey = new SecretKeySpec(key, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return cipher.doFinal(data);
	}
}