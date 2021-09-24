package net.imyeyu.blogapi.controller;

import lombok.extern.slf4j.Slf4j;
import net.imyeyu.betterjava.BetterJava;
import net.imyeyu.blogapi.annotation.AOPLog;
import net.imyeyu.blogapi.annotation.QPSLimit;
import net.imyeyu.blogapi.annotation.RequiredToken;
import net.imyeyu.blogapi.bean.CaptchaData;
import net.imyeyu.blogapi.bean.Response;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.bean.SettingsKey;
import net.imyeyu.blogapi.entity.User;
import net.imyeyu.blogapi.entity.UserConfig;
import net.imyeyu.blogapi.entity.UserData;
import net.imyeyu.blogapi.entity.UserPrivacy;
import net.imyeyu.blogapi.service.CommentReplyRecordService;
import net.imyeyu.blogapi.service.CommentReplyService;
import net.imyeyu.blogapi.service.CommentService;
import net.imyeyu.blogapi.service.SettingsService;
import net.imyeyu.blogapi.service.UserConfigService;
import net.imyeyu.blogapi.service.UserDataService;
import net.imyeyu.blogapi.service.UserPrivacyService;
import net.imyeyu.blogapi.service.UserService;
import net.imyeyu.blogapi.util.Captcha;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户接口
 *
 * <p>夜雨 创建于 2021-02-23 21:38
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends BaseController implements BetterJava {

	@Autowired
	private UserService service;

	@Autowired
	private UserDataService dataService;

	@Autowired
	private UserPrivacyService privacyService;

	@Autowired
	private UserConfigService configService;

	@Autowired
	private SettingsService settingsService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private CommentReplyService commentReplyService;

	@Autowired
	private CommentReplyRecordService commentReplyRecordService;

	/**
	 * 注册用户
	 *
	 * @param request     请求体
	 * @param captchaData 含数据体和验证码请求对象，数据体为 User，至少包含用户名和密码
	 * @return true 为注册成功
	 */
	@AOPLog
	@QPSLimit
	@PostMapping("/register")
	public Response<?> register(@RequestBody CaptchaData<User> captchaData, HttpServletRequest request) {
		try {
			// 功能状态
			if (settingsService.not(SettingsKey.ENABLE_REGISTER)) {
				return new Response<>(ReturnCode.ERROR_OFF_SERVICE, "注册服务未启用");
			}
			User user = captchaData.getData();
			if (ObjectUtils.isEmpty(user)) {
				return new Response<>(ReturnCode.REQUEST_BAD, "无效的请求");
			}
			// 验证码
			Captcha.test(captchaData.getCaptcha(), "REGISTER");
			// 创建用户
			return new Response<>(ReturnCode.SUCCESS, service.register(user));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			log.error("UserController.register", e);
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * <p>用户登录
	 * <p>user 可以是 uid、邮箱或用户名
	 *
	 * @param params  含 user, password 和 captcha
	 * @return 通信令牌
	 */
	@AOPLog
	@QPSLimit
	@PostMapping("/sign-in")
	public Response<?> signIn(@RequestBody Map<String, String> params) {
		try {
			// 功能状态
			if (settingsService.not(SettingsKey.ENABLE_LOGIN)) {
				return new Response<>(ReturnCode.ERROR_OFF_SERVICE, "登录服务未启用");
			}
			// 用户
			if (StringUtils.isEmpty(params.get("user"))) {
				return new Response<>(ReturnCode.PARAMS_MISS, "请输入 UID、邮箱或用户名");
			}
			// 密码
			if (StringUtils.isEmpty(params.get("password"))) {
				return new Response<>(ReturnCode.PARAMS_MISS, "请输入密码");
			}
			// 验证码
			Captcha.test(params.get("captcha"), "SIGNIN");
			// 执行登录
			return new Response<>(ReturnCode.SUCCESS, service.signIn(params.get("user"), params.get("password")));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			log.error("UserController.signIn", e);
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 返回令牌是否有效
	 *
	 * @param token 令牌
	 * @return true 为有效
	 */
	@AOPLog
	@QPSLimit
	@RequiredToken
	@PostMapping("/sign-in/status")
	public Response<?> isSignedIn(@RequestHeader("Token") String token) {
		try {
			return new Response<>(ReturnCode.SUCCESS, service.isSignedIn(token));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 退出登录
	 *
	 * @param token 令牌
	 * @return true 为退出成功
	 */
	@AOPLog
	@RequiredToken
	@PostMapping("/sign-out")
	public Response<?> signOut(@RequestHeader("Token") String token) {
		try {
			return new Response<>(ReturnCode.SUCCESS, service.signOut(token));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			log.error("UserController.signOut", e);
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 发送邮箱验证邮件
	 *
	 * @param token 令牌
	 * @return true 为添加队列成功
	 */
	@AOPLog
	@QPSLimit(50000)
	@RequiredToken
	@PostMapping("/email/verify")
	public Response<?> sendEmailVerify(@RequestHeader("Token") String token) {
		try {
			return new Response<>(ReturnCode.SUCCESS, service.sendEmailVerify(token2UID(token)));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("UserController.sendEmailVerify", e);
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 邮箱验证回调（用户点击邮件的验证跳转前端，再由前端调用）
	 *
	 * @param params 含 key 邮箱验证密钥（非登录令牌）
	 * @param token  令牌
	 * @return true 为添加队列成功
	 */
	@AOPLog
	@QPSLimit(10000)
	@RequiredToken
	@PostMapping("/email/verify/callback")
	public Response<?> emailVerifyCallback(@RequestBody Map<String, String> params, @RequestHeader("Token") String token) {
		try {
			if (StringUtils.isEmpty(params.get("key"))) {
				return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：key");
			}
			return new Response<>(ReturnCode.SUCCESS, service.emailVerifyCallback(token2UID(token), params.get("key")));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("UserController.sendEmailVerify", e);
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 修改密码（会清除登录会话）
	 *
	 * @param params oldPW 旧密码，newPW 新密码
	 * @return true 为修改成功
	 */
	@AOPLog
	@QPSLimit
	@RequiredToken
	@PostMapping("/password/update")
	public Response<?> updatePassword(@RequestBody Map<String, String> params, @RequestHeader("Token") String token) {
		try {
			String oldPassword = params.get("oldPassword");
			if (StringUtils.isEmpty(oldPassword)) {
				return new Response<>(ReturnCode.PARAMS_MISS, "请输入旧密码");
			}
			String newPassword = params.get("newPassword");
			if (StringUtils.isEmpty(newPassword)) {
				return new Response<>(ReturnCode.PARAMS_MISS, "请输入新密码");
			}
			return new Response<>(ReturnCode.SUCCESS, service.updatePassword(token2UID(token), oldPassword, newPassword));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			log.error("UserController.updatePassword", e);
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 注销用户
	 *
	 * @param params 明文密码
	 * @return true 为成功
	 */
	@AOPLog
	@QPSLimit
	@RequiredToken
	@PostMapping("/cancel")
	public Response<?> cancel(@RequestBody Map<String, String> params, @RequestHeader("Token") String token) {
		try {
			if (StringUtils.isEmpty(params.get("password"))) {
				return new Response<>(ReturnCode.PERMISSION_ERROR, "请输入密码");
			}
			return new Response<>(ReturnCode.SUCCESS, service.cancel(token2UID(token), params.get("password")));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			log.error("UserController.cancel", e);
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 获取用户资料
	 * <p>如果目标资料和令牌用户相同，不过滤用户资料
	 *
	 * @param id    目标用户 ID
	 * @param token 令牌（可选）
	 * @return 用户资料
	 */
	@AOPLog
	@QPSLimit(100)
	@PostMapping("/data/{id}")
	public Response<?> getData(@PathVariable Long id, @RequestHeader("Token") String token) {
		if (ObjectUtils.isEmpty(id)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "未知用户 ID");
		}
		try {
			boolean needFilter = true;
			if (!StringUtils.isEmpty(token)) {
				// 未登录，并且获取的用户资料不是自己的，执行隐私控制过滤
				needFilter = !service.isSignedIn(token) || !id.equals(token2UID(token));
			}
			UserData data = dataService.findByUID(id).withUser();
			if (needFilter) {
				data = data.filterPrivacy(privacyService.findByUID(id));
			}
			return new Response<>(ReturnCode.SUCCESS, data);
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			log.error("UserController.getData", e);
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 更新用户数据
	 *
	 * @param data  用户数据（包括账号数据）
	 * @param token 令牌
	 * @return true 为更新成功
	 */
	@AOPLog
	@QPSLimit
	@RequiredToken
	@PostMapping("/data/update")
	public Response<?> updateData(@RequestBody UserData data, @RequestHeader("Token") String token) {
		try {
			if (settingsService.not(SettingsKey.ENABLE_USER_UPDATE)) {
				return new Response<>(ReturnCode.ERROR_OFF_SERVICE, "用户资料更新服务未启用");
			}
			Long uid = token2UID(token);
			data.setUserId(uid);
			data.getUser().setId(uid);
			// 更新账号
			User user = service.find(uid);
			if (!user.getEmail().equals(data.getUser().getEmail())) {
				// 修改了邮箱，强制重新验证
				data.getUser().setEmailVerify(false);
			}
			service.update(data.getUser());
			// 更新资料
			dataService.updateData(data);
			return new Response<>(ReturnCode.SUCCESS, true);
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (NullPointerException e) {
			return new Response<>(ReturnCode.REQUEST_BAD, "无效的请求");
		} catch (Exception e) {
			log.error("UserController.updateData", e);
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 获取用户隐私控制
	 *
	 * @return 用户资料
	 */
	@AOPLog
	@QPSLimit
	@RequiredToken
	@PostMapping("/privacy")
	public Response<?> getPrivacy(@RequestHeader("Token") String token) {
		try {
			return new Response<>(ReturnCode.SUCCESS, privacyService.findByUID(token2UID(token)));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			log.error("UserController.getPrivacy", e);
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 更新用户隐私控制
	 *
	 * @param token 含令牌用户隐私控制数据
	 * @return true 为更新成功
	 */
	@AOPLog
	@QPSLimit
	@RequiredToken
	@PostMapping("/privacy/update")
	public Response<?> updatePrivacy(@RequestBody UserPrivacy privacy, @RequestHeader("Token") String token) {
		try {
			privacy.setUserId(token2UID(token));
			privacyService.update(privacy);
			return new Response<>(ReturnCode.SUCCESS, true);
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("UserController.updatePrivacy", e);
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 获取用户设置
	 *
	 * @return 用户设置
	 */
	@AOPLog
	@QPSLimit
	@RequiredToken
	@PostMapping("/config")
	public Response<?> getConfig(@RequestHeader("Token") String token) {
		try {
			return new Response<>(ReturnCode.SUCCESS, configService.findByUID(token2UID(token)));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			log.error("UserController.getSetting", e);
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 更新用户设置
	 *
	 * @param config 用户设置
	 * @param token  令牌
	 * @return true 为更新成功
	 */
	@AOPLog
	@QPSLimit
	@RequiredToken
	@PostMapping("/config/update")
	public Response<?> updateConfig(@RequestBody UserConfig config, @RequestHeader("Token") String token) {
		try {
			config.setUserId(token2UID(token));
			configService.update(config);
			return new Response<>(ReturnCode.SUCCESS, true);
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("UserController.updateSetting", e);
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 获取用户评论（包括回复）
	 * <p>令牌是可选的，如果目标用户数据和令牌相同，则不考虑隐私控制，否则需要根据隐私控制判定是否返回
	 *
	 * @param id     目标用户 ID
	 * @param offset 偏移
	 * @param token  令牌
	 * @return 用户评论列表
	 */
	@AOPLog
	@QPSLimit
	@PostMapping("/comment/{id}")
	public Response<?> getComments(@PathVariable Long id, @RequestParam Long offset, @RequestHeader("Token") String token) {
		try {
			if (StringUtils.isEmpty(token) || !token2UID(token).equals(id)) {
				UserPrivacy userPrivacy = privacyService.findByUID(id);
				if (!userPrivacy.isComment()) {
					// 不是获取自己的评论，且目标用户评论不公开
					return new Response<>(ReturnCode.PERMISSION_ERROR, "该用户评论不公开");
				}
			}
			return new Response<>(ReturnCode.SUCCESS, service.findManyUserComment(id, offset, 12));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("UserController.getComments", e);
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 获取用户被回复的评论
	 *
	 * @param offset 偏移
	 * @param token  令牌
	 * @return 用户评论列表
	 */
	@AOPLog
	@QPSLimit
	@RequiredToken
	@PostMapping("/comment/reply")
	public Response<?> getCommentReply(@RequestParam Long offset, @RequestHeader("Token") String token) {
		try {
			return new Response<>(ReturnCode.SUCCESS, commentReplyRecordService.findManyByUID(token2UID(token), offset, 12));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("UserController.getCommentReply", e);
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 删除评论或回复（由"我的评论"调用）
	 *
	 * @param params 含 cid 或 crid
	 * @param token  令牌
	 * @return true 为删除成功
	 */
	@AOPLog
	@QPSLimit
	@RequiredToken
	@PostMapping("/comment/del")
	public Response<?> delComment(@RequestBody Map<String, String> params, @RequestHeader("Token") String token) {
		try {
			Long uid = token2UID(token);
			if (ObjectUtils.isEmpty(params.get("crid"))) {
				Long cid = Long.parseLong(params.get("cid"));
				if (!commentService.find(cid).getUserId().equals(uid)) {
					return new Response<>(ReturnCode.PERMISSION_ERROR, "无效的令牌，无权限操作");
				}
				commentService.delete(cid);
			} else {
				Long crid = Long.parseLong(params.get("crid"));
				if (!commentReplyService.find(crid).getSenderId().equals(uid)) {
					return new Response<>(ReturnCode.PERMISSION_ERROR, "无效的令牌，无权限操作");
				}
				commentReplyService.delete(crid);
			}
			return new Response<>(ReturnCode.SUCCESS, true);
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("UserController.delComment", e);
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 删除被回复通知
	 *
	 * @param params 含 crid
	 * @param token  令牌
	 * @return true 为删除成功
	 */
	@AOPLog
	@QPSLimit
	@RequiredToken
	@PostMapping("/reply/del")
	public Response<?> delReplyRecord(@RequestBody Map<String, String> params, @RequestHeader("Token") String token) {
		try {
			if (StringUtils.isEmpty(params.get("crid"))) {
				return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：crid");
			}
			commentReplyRecordService.deleteByRIDwithUID(token2UID(token), Long.parseLong(params.get("crid")));
			return new Response<>(ReturnCode.SUCCESS, true);
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("UserController.delReplyRecord", e);
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 修改头像
	 *
	 * @param file  文件
	 * @param token 令牌
	 * @return 头像资源路径
	 */
	@AOPLog
	@QPSLimit
	@RequiredToken
	@PostMapping("/avatar/update")
	public Response<?> updateAvatar(@RequestParam("file") MultipartFile file, @RequestHeader("Token") String token) {
		try {
			if (settingsService.not(SettingsKey.ENABLE_USER_UPDATE)) {
				return new Response<>(ReturnCode.ERROR_OFF_SERVICE, "用户资料更新服务未启用");
			}
			if (file.isEmpty()) {
				return new Response<>(ReturnCode.PARAMS_MISS, "缺少文件：file");
			}
			if (file.getOriginalFilename() != null && !file.getOriginalFilename().endsWith(".png")) {
				return new Response<>(ReturnCode.PARAMS_BAD, "仅支持 PNG 图像文件");
			}
			return new Response<>(ReturnCode.SUCCESS, dataService.updateAvatar(token2UID(token), file));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("UserController.updateAvatar", e);
			return new Response<>(ReturnCode.ERROR, "服务异常：" + e.getMessage());
		}
	}

	/**
	 * 修改背景图
	 *
	 * @param file  文件
	 * @param token 令牌
	 * @return 背景图资源路径
	 */
	@AOPLog
	@QPSLimit
	@RequiredToken
	@PostMapping("/wrapper/update")
	public Response<?> updateWrapper(@RequestParam("file") MultipartFile file, @RequestHeader("Token") String token) {
		try {
			if (settingsService.not(SettingsKey.ENABLE_USER_UPDATE)) {
				return new Response<>(ReturnCode.ERROR_OFF_SERVICE, "用户资料更新服务未启用");
			}
			if (file.isEmpty()) {
				return new Response<>(ReturnCode.PARAMS_MISS, "缺少文件：file");
			}
			if (file.getOriginalFilename() != null && !file.getOriginalFilename().endsWith(".png")) {
				return new Response<>(ReturnCode.PARAMS_BAD, "仅支持 PNG 图像文件");
			}
			return new Response<>(ReturnCode.SUCCESS, null, dataService.updateWrapper(token2UID(token), file));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
			log.error("UserController.updateWrapper", e);
			return new Response<>(ReturnCode.ERROR, "服务异常：" + e.getMessage());
		}
	}

	/**
	 * 提取令牌 UID
	 *
	 * @param token 令牌
	 * @return UID
	 * @throws ServiceException 服务异常
	 */
	private Long token2UID(String token) throws ServiceException {
		try {
			return Long.parseLong(token.substring(0, token.indexOf("_")));
		} catch (Exception e) {
			throw new ServiceException(ReturnCode.PARAMS_BAD, "无效的令牌");
		}
	}
}