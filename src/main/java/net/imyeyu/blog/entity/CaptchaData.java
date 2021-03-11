package net.imyeyu.blog.entity;

/**
 * 含验证码数据实体
 *
 * 夜雨 创建于 2021/3/1 17:10
 */
public class CaptchaData<T> {

	private String captcha;
	private T t;

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	@Override
	public String toString() {
		return "CaptchaData{" + "captcha='" + captcha + '\'' + ", t=" + t + '}';
	}
}