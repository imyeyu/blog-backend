package net.imyeyu.blog.util;

import lombok.Getter;
import net.imyeyu.blog.bean.ReturnCode;
import net.imyeyu.blog.bean.ServiceException;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 验证码绘制
 *
 * 夜雨 创建于 2021-05-20 16:48
 */
public class Captcha {

	private static final Random RANDOM = new Random();

	@Getter private final StringBuilder code;
	@Getter private final BufferedImage image;

	public Captcha(int width, int height) {
		// 图片流
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) image.getGraphics();
		// 绘制背景
		g.setColor(new Color(244, 244, 244));
		g.fillRect(0, 0, width, height);
		// 绘制数字
		code = new StringBuilder();
		char[] c = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
				'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'F',
				'G', 'H', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		// 移除 O,o,I,l
		g.setFont(new Font("Fixedsys", Font.BOLD, 20));
		for (int i = 0; i < 4; i++) {
			g.setColor(new Color(RANDOM.nextInt(155), RANDOM.nextInt(155), RANDOM.nextInt(155)));
			// 随机角度
			int degree = RANDOM.nextInt() % 20;
			// 随机字符
			int n = RANDOM.nextInt(c.length);
			// 倾斜
			g.rotate(degree * Math.PI / 180, 5 + 16 * i, 18);
			// 绘制字符
			g.drawString(c[n] + "", 8 + 16 * i, 16);
			// 回正
			g.rotate(-degree * Math.PI / 180, 5 + 16 * i, 18);
			code.append(c[n]);
		}
		// 绘制干扰线
		for (int i = 0; i < 10; i++) {
			g.setColor(new Color(RANDOM.nextInt(155), RANDOM.nextInt(155), RANDOM.nextInt(155)));
			g.drawLine(RANDOM.nextInt(width), RANDOM.nextInt(height), RANDOM.nextInt(width), RANDOM.nextInt(height));
		}
	}

	/** @return 错误回调图像 */
	public static BufferedImage error(ReturnCode code) {
		final int width = 74, height = 24;
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D) img.getGraphics();
		g.setColor(Color.RED);
		// 文本
		g.setFont(new Font("Fixedsys", Font.BOLD, 13));
		g.drawString("ERR." + code.getCode(), 2, 18);
		g.setColor(Color.DARK_GRAY);
		return img;
	}

	/**
	 * <p>验证。通过验证不抛出任何异常也不返回任何内容，否则抛出相应异常
	 * <p>会验证非空和期限
	 *
	 * @param captcha 提交的验证码
	 * @param from    来自模块
	 * @throws ServiceException 验证异常
	 */
	public static void test(String captcha, String from) throws ServiceException {
		if (StringUtils.isEmpty(captcha)) {
			throw new ServiceException(ReturnCode.PARAMS_MISS, "请输入验证码");
		}
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (sra != null) {
			HttpSession session = sra.getRequest().getSession();
			// Session 验证
			Object sessionObject = session.getAttribute(from + "_CAPTCHA");
			if (ObjectUtils.isEmpty(sessionObject)) {
				throw new ServiceException(ReturnCode.PARAMS_EXPIRD, "验证码已过期");
			}
			if (!captcha.equalsIgnoreCase(sessionObject.toString())) {
				// 不通过，清除缓存
				clear(session, from);
				throw new ServiceException(ReturnCode.PARAMS_BAD, "验证码错误");
			}
		} else {
			throw new ServiceException(ReturnCode.PARAMS_BAD, "验证码错误");
		}
	}

	/**
	 * 清除验证码
	 *
	 * @param session Session
	 * @param from    验证码标记
	 */
	public static void clear(HttpSession session, String from) {
		session.removeAttribute(from + "_CAPTCHA");
	}
}