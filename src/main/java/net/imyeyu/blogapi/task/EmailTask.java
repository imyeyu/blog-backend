package net.imyeyu.blogapi.task;

import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import net.imyeyu.betterjava.Encode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.CommentRemindQueue;
import net.imyeyu.blogapi.entity.EmailQueue;
import net.imyeyu.blogapi.entity.EmailQueueLog;
import net.imyeyu.blogapi.entity.User;
import net.imyeyu.blogapi.service.ArticleService;
import net.imyeyu.blogapi.service.CommentRemindQueueService;
import net.imyeyu.blogapi.service.CommentReplyService;
import net.imyeyu.blogapi.service.CommentService;
import net.imyeyu.blogapi.service.EmailQueueService;
import net.imyeyu.blogapi.service.UserService;
import net.imyeyu.blogapi.util.AES;
import net.imyeyu.blogapi.util.Redis;
import net.imyeyu.blogapi.util.Token;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 邮件推送任务
 *
 * <p>夜雨 创建于 2021-08-24 14:10
 */
@Slf4j
@Configuration
@EnableScheduling
public class EmailTask {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Value("${spring.mail.username}")
	private String sendUser;

	@Autowired
	private AES aes;

	@Autowired
	private EmailQueueService service;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommentRemindQueueService commentRemindQueueService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private CommentReplyService commentReplyService;

	@Autowired
	private Token token;

	@Autowired
	private Redis<Long, String> userEmailVerify;

	@Scheduled(fixedRate = 12000)
	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
	private void traverseQueue() {
		try {
			List<EmailQueue> emailQueueList = service.findAll();
			if (0 < emailQueueList.size()) {
				long now = System.currentTimeMillis();
				for (EmailQueue emailQueue : emailQueueList) {
					if (emailQueue.getSendAt() < now) {
						log.info(emailQueue.getUUID() + " 邮件推送：" + emailQueue.getType() + "." +emailQueue.getDataId());

						EmailQueueLog emailQueueLog = new EmailQueueLog();
						emailQueueLog.setUUID(emailQueue.getUUID());
						emailQueueLog.setType(emailQueue.getType());
						emailQueueLog.setDataId(emailQueue.getDataId());
						emailQueueLog.setSendAt(emailQueue.getSendAt());
						try {
							switch (emailQueue.getType()) {
								case REPLY_REMINAD  -> emailQueueLog.setSendTo(sendEmail4ReplyRemind(emailQueue));
								case EMAIL_VERIFY   -> emailQueueLog.setSendTo(sendEmail4EmailVerify(emailQueue));
								case RESET_PASSWORD -> emailQueueLog.setSendTo(sendEmail4ResetPassword(emailQueue));
							}
							emailQueueLog.setIsSent(true);
							emailQueueLog.setCreatedAt(System.currentTimeMillis());
							log.info(emailQueue.getUUID() + " 邮件推送成功：" + (emailQueueLog.getCreatedAt() - now) + " ms");
						} catch (Exception e) {
							emailQueueLog.setIsSent(false);
							log.error(emailQueue.getUUID() + " 邮件推送异常", e);
							emailQueueLog.setExceptionMsg(e.getMessage());
						}
						service.addLog(emailQueueLog);
						service.delete(emailQueue.getUUID());
					}
				}
			}
		} catch (ServiceException e) {
			log.error("邮件推送服务异常", e);
		} catch (Exception e) {
			log.error("邮件推送异常", e);
		}
	}

	/**
	 * 发送邮件
	 *
	 * @param to      目标邮箱
	 * @param subject 标题
	 * @param html    HTML 字符串
	 * @throws Exception 发送异常
	 */
	private void sendEmail(String to, String subject, String html) throws Exception {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom(sendUser);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(html, true);
		mailSender.send(message);
	}

	/**
	 * 邮箱验证
	 *
	 * @param emailQueue 邮件队列
	 * @return 发送目标
	 * @throws Exception 服务异常
	 */
	private String sendEmail4EmailVerify(EmailQueue emailQueue) throws Exception {
		User user = userService.find(emailQueue.getDataId()).withData();

		String key = Encode.md5(aes.encrypt(new SecureRandom().nextLong() + user.getEmail()));
		userEmailVerify.set(user.getId(), key, 600L);

		Map<String, Object> model = new HashMap<>();
		model.put("user", user);
		model.put("url", "https://www.imyeyu.net/user/space/" + user.getId() + "?action=EMAIL_VERIFY&key=" + key);

		Template template = freeMarkerConfigurer.getConfiguration().getTemplate("EmailVerify.ftl");
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

		sendEmail(user.getEmail(), "Hey! 请继续完成 夜雨博客 的邮箱验证!", html);
		return user.getEmail();
	}

	/**
	 * 回复提醒邮件
	 *
	 * @param emailQueue 邮件队列
	 * @return 发送目标
	 * @throws Exception 服务异常
	 */
	private String sendEmail4ReplyRemind(EmailQueue emailQueue) throws Exception {
		User user = userService.find(emailQueue.getDataId()).withData();
		List<CommentRemindQueue> reminds = commentRemindQueueService.findManyByUID(emailQueue.getDataId());
		// 回查数据
		for (CommentRemindQueue remind : reminds) {
			// 回复
			remind.withReply();
			if (!ObjectUtils.isEmpty(remind.getReply().getSenderId())) {
				// 发送者
				remind.getReply().withSender();
			}
		}

		Map<String, Object> model = new HashMap<>();
		model.put("user", user);
		model.put("reminds", reminds);

		Template template = freeMarkerConfigurer.getConfiguration().getTemplate("ReplyRemind.ftl");
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

		sendEmail(user.getEmail(), "Hey! 你在 夜雨博客 的评论收到新回复", html);
		// 移除回复提醒队列
		commentRemindQueueService.deleteByUID(emailQueue.getDataId());
		return user.getEmail();
	}

	private String sendEmail4ResetPassword(EmailQueue emailQueue) throws Exception {
		User user = userService.find(emailQueue.getDataId());

		Map<String, Object> model = new HashMap<>();
		Template template = freeMarkerConfigurer.getConfiguration().getTemplate("ResetPassword.ftl");
		String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		sendEmail(user.getEmail(), "重置密码", html);
		return user.getEmail();
	}
}
