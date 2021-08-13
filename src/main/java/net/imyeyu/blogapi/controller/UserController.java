package net.imyeyu.blogapi.controller;

import net.imyeyu.betterjava.BetterJava;
import net.imyeyu.blogapi.annotation.AOPLog;
import net.imyeyu.blogapi.annotation.AccessLimit;
import net.imyeyu.blogapi.bean.CaptchaData;
import net.imyeyu.blogapi.bean.Response;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.bean.SystemKey;
import net.imyeyu.blogapi.bean.TokenData;
import net.imyeyu.blogapi.entity.User;
import net.imyeyu.blogapi.entity.UserData;
import net.imyeyu.blogapi.entity.UserPrivacy;
import net.imyeyu.blogapi.entity.UserSettings;
import net.imyeyu.blogapi.service.SystemService;
import net.imyeyu.blogapi.service.UserDataService;
import net.imyeyu.blogapi.service.UserPrivacyService;
import net.imyeyu.blogapi.service.UserService;
import net.imyeyu.blogapi.service.UserSettingsService;
import net.imyeyu.blogapi.util.Captcha;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RestController
@RequestMapping("/user")
public class UserController extends BaseController implements BetterJava {

	@Autowired
	private UserService service;

	@Autowired
	private UserDataService dataService;

	@Autowired
	private UserPrivacyService privacyService;

	private UserSettingsService settingService;

	@Autowired
	private SystemService systemService;

	/**
	 * 注册用户
	 *
	 * @param request     请求体
	 * @param captchaData 含数据体和验证码请求对象，数据体为 User，至少包含用户名和密码
	 * @return true 为注册成功
	 */
	@AOPLog
	@AccessLimit(time = 3000, needLogin = false)
	@PostMapping("/register")
	public Response<?> register(@RequestBody CaptchaData<User> captchaData, HttpServletRequest request) {
		try {
			// 功能状态
			if (systemService.not(SystemKey.ENABLE_REGISTER)) {
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
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * <p>用户登录
	 * <p>user 可以是 uid、邮箱或用户名
	 *
	 * @param params  含 user, password 和 captcha
	 * @param request 请求体
	 * @return true 为登录成功
	 */
	@AOPLog
	@AccessLimit(time = 3000, needLogin = false)
	@PostMapping("/sign-in")
	public Response<?> signIn(@RequestBody Map<String, String> params, HttpServletRequest request) {
		try {
			// 功能状态
			if (systemService.not(SystemKey.ENABLE_LOGIN)) {
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
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 返回该 ID 用户是否已登录
	 *
	 * @param params 令牌
	 * @return true 为已登录
	 */
	@AOPLog
	@AccessLimit(time = 3000, needLogin = false)
	@PostMapping("/sign-in/status")
	public Response<?> isSignedIn(@RequestBody Map<String, String> params) {
		if (StringUtils.isEmpty(params.get("token"))) {
			return new Response<>(ReturnCode.PERMISSION_ERROR, "无效的令牌，无权限操作");
		}
		try {
			return new Response<>(ReturnCode.SUCCESS, service.isSignedIn(params.get("token")));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 退出登录
	 *
	 * @param params 含 uid 和 token
	 * @return true 为退出成功
	 */
	@AOPLog
	@PostMapping("/sign-out")
	public Response<?> signOut(@RequestBody Map<String, String> params) {
		if (StringUtils.isEmpty(params.get("token"))) {
			return new Response<>(ReturnCode.PERMISSION_ERROR, "无效的令牌，无权限操作");
		}
		try {
			return new Response<>(ReturnCode.SUCCESS, service.signOut(params.get("token")));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 获取用户资料
	 * <p>如果目标资料和令牌用户相同，不过滤用户资料
	 *
	 * @param id     目标用户 ID
	 * @param params 令牌
	 * @return 用户资料
	 */
	@AOPLog
	@PostMapping("/{id}")
	public Response<?> getData(@PathVariable Long id, @RequestBody Map<String, String> params) {
		if (ObjectUtils.isEmpty(id)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：id");
		}
		try {
			String token = params.get("token");
			boolean needFilter = true;
			if (!StringUtils.isEmpty(token)) {
				// 已登录，并且获取的用户资料是自己的，不执行隐私控制过滤
				Long fromUID = Long.parseLong(token.substring(0, token.indexOf("#")));
				needFilter = !service.isSignedIn(token) || !id.equals(fromUID);
			}
			UserData data = dataService.findByUID(id);
			User user = service.find(id);
			user.setPassword(null);
			data.setUser(user);
			if (needFilter) {
				data = data.filterPrivacy(privacyService.findByUID(id));
			}
			return new Response<>(ReturnCode.SUCCESS, data);
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 更新用户数据
	 *
	 * @param td 含令牌用户数据（包括账号数据）
	 * @return true 为更新成功
	 */
	@AOPLog
	@AccessLimit(time = 3000)
	@PostMapping("/update/data")
	public Response<?> updateData(@RequestBody TokenData<UserData> td) {
		try {
			if (systemService.not(SystemKey.ENABLE_USER_UPDATE) || systemService.not(SystemKey.ENABLE_USER_DATA_UPDATE)) {
				return new Response<>(ReturnCode.ERROR_OFF_SERVICE, "用户资料更新服务未启用");
			}
			String token = td.getToken();
			if (StringUtils.isEmpty(token)) {
				return new Response<>(ReturnCode.PERMISSION_ERROR, "无效的令牌，无权限操作");
			}
			if (!service.isSignedIn(token)) {
				return new Response<>(ReturnCode.PERMISSION_MISS, "未登录，无权限操作");
			}
			UserData data = td.getData();
			Long tokenUID = Long.parseLong(token.substring(0, token.indexOf("#")));
			if (!tokenUID.equals(data.getUserId())) {
				return new Response<>(ReturnCode.PERMISSION_ERROR, "无效的令牌，无权限操作");
			}
			// 更新账号
			service.update(data.getUser());
			// 更新资料
			dataService.updateData(data);
			return new Response<>(ReturnCode.SUCCESS, true);
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 获取用户隐私控制
	 *
	 * @param id     目标用户 ID
	 * @param params 令牌
	 * @return 用户资料
	 */
	@AOPLog
	@AccessLimit(time = 3000)
	@PostMapping("/privacy/{id}")
	public Response<?> getPrivacy(@PathVariable Long id, @RequestBody Map<String, String> params) {
		if (ObjectUtils.isEmpty(id)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：id");
		}
		try {
			String token = params.get("token");
			if (StringUtils.isEmpty(token)) {
				return new Response<>(ReturnCode.PERMISSION_ERROR, "无效的令牌，无权限操作");
			}
			if (!service.isSignedIn(token)) {
				return new Response<>(ReturnCode.PERMISSION_MISS, "未登录，无权限操作");
			}
			return new Response<>(ReturnCode.SUCCESS, privacyService.findByUID(id));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 更新用户隐私控制
	 *
	 * @param td 含令牌用户隐私控制数据
	 * @return true 为更新成功
	 */
	@AOPLog
	@AccessLimit(time = 3000)
	@PostMapping("/update/privacy")
	public Response<?> updatePrivacy(@RequestBody TokenData<UserPrivacy> td) {
		try {
			String token = td.getToken();
			if (StringUtils.isEmpty(token)) {
				return new Response<>(ReturnCode.PERMISSION_ERROR, "无效的令牌，无权限操作");
			}
			if (!service.isSignedIn(token)) {
				return new Response<>(ReturnCode.PERMISSION_MISS, "未登录，无权限操作");
			}
			UserPrivacy privacy = td.getData();
			Long tokenUID = Long.parseLong(token.substring(0, token.indexOf("#")));
			if (!tokenUID.equals(privacy.getUserId())) {
				return new Response<>(ReturnCode.PERMISSION_ERROR, "无权限操作");
			}
			// 更新资料
			privacyService.update(privacy);
			return new Response<>(ReturnCode.SUCCESS, true);
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 获取用户设置
	 *
	 * @param id     目标用户设置
	 * @param params 令牌
	 * @return 用户设置
	 */
	@AOPLog
	@AccessLimit(time = 3000)
	@PostMapping("/setting/{id}")
	public Response<?> getSetting(@PathVariable Long id, @RequestBody Map<String, String> params) {
		if (ObjectUtils.isEmpty(id)) {
			return new Response<>(ReturnCode.PARAMS_MISS, "缺少参数：id");
		}
		try {
			String token = params.get("token");
			if (StringUtils.isEmpty(token)) {
				return new Response<>(ReturnCode.PERMISSION_ERROR, "无效的令牌，无权限操作");
			}
			if (!service.isSignedIn(token)) {
				return new Response<>(ReturnCode.PERMISSION_MISS, "未登录，无权限操作");
			}
			return new Response<>(ReturnCode.SUCCESS, settingService.findByUID(id));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response<>(ReturnCode.ERROR, e);
		}
	}

	/**
	 * 更新用户设置
	 *
	 * @param td 含令牌用户设置
	 * @return true 为更新成功
	 */
	@AOPLog
	@AccessLimit(time = 3000)
	@PostMapping("/update/setting")
	public Response<?> updateSetting(@RequestBody TokenData<UserSettings> td) {
		try {
			String token = td.getToken();
			if (StringUtils.isEmpty(token)) {
				return new Response<>(ReturnCode.PERMISSION_ERROR, "无效的令牌，无权限操作");
			}
			if (!service.isSignedIn(token)) {
				return new Response<>(ReturnCode.PERMISSION_MISS, "未登录，无权限操作");
			}
			UserSettings setting = td.getData();
			Long tokenUID = Long.parseLong(token.substring(0, token.indexOf("#")));
			if (!tokenUID.equals(setting.getUserId())) {
				return new Response<>(ReturnCode.PERMISSION_ERROR, "无权限操作");
			}
			// 更新资料
			settingService.update(setting);
			return new Response<>(ReturnCode.SUCCESS, true);
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
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
	@AccessLimit(time = 3000)
	@PostMapping("/update/avatar")
	public Response<?> updateAvatar(@RequestParam("file") MultipartFile file, @RequestParam("token") String token) {
		try {
			if (systemService.not(SystemKey.ENABLE_USER_DATA_UPDATE)) {
				return new Response<>(ReturnCode.ERROR_OFF_SERVICE, "用户资料更新服务未启用");
			}
			if (StringUtils.isEmpty(token)) {
				return new Response<>(ReturnCode.PERMISSION_ERROR, "无效的令牌，无权限操作");
			}
			if (!service.isSignedIn(token)) {
				return new Response<>(ReturnCode.PERMISSION_MISS, "未登录，无权限操作");
			}
			if (file.isEmpty()) {
				return new Response<>(ReturnCode.PARAMS_MISS, "缺少文件：file");
			}
			if (file.getOriginalFilename() != null && !file.getOriginalFilename().endsWith(".png")) {
				return new Response<>(ReturnCode.PARAMS_BAD, "仅支持 PNG 图像文件");
			}
			Long id = Long.parseLong(token.split("#")[0]);
			return new Response<>(ReturnCode.SUCCESS, dataService.updateAvatar(id, file));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
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
	@AccessLimit(time = 3000)
	@PostMapping("/update/wrapper")
	public Response<?> updateWrapper(@RequestParam("file") MultipartFile file, @RequestParam("token") String token) {
		try {
			if (systemService.not(SystemKey.ENABLE_USER_DATA_UPDATE)) {
				return new Response<>(ReturnCode.ERROR_OFF_SERVICE, "用户资料更新服务未启用");
			}
			if (StringUtils.isEmpty(token)) {
				return new Response<>(ReturnCode.PERMISSION_ERROR, "无效的令牌，无权限操作");
			}
			if (!service.isSignedIn(token)) {
				return new Response<>(ReturnCode.PERMISSION_MISS, "未登录，无权限操作");
			}
			if (file.isEmpty()) {
				return new Response<>(ReturnCode.PARAMS_MISS, "缺少文件：file");
			}
			if (file.getOriginalFilename() != null && !file.getOriginalFilename().endsWith(".png")) {
				return new Response<>(ReturnCode.PARAMS_BAD, "仅支持 PNG 图像文件");
			}
			Long id = Long.parseLong(token.split("#")[0]);
			return new Response<>(ReturnCode.SUCCESS, null, dataService.updateWrapper(id, file));
		} catch (ServiceException e) {
			return new Response<>(e.getCode(), e.getMessage());
		} catch (Exception e) {
			return new Response<>(ReturnCode.ERROR, "服务异常：" + e.getMessage());
		}
	}
}