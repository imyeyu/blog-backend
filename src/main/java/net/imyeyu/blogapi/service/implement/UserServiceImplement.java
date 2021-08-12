package net.imyeyu.blogapi.service.implement;

import lombok.extern.slf4j.Slf4j;
import net.imyeyu.betterjava.BetterJava;
import net.imyeyu.betterjava.Encode;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.User;
import net.imyeyu.blogapi.entity.UserData;
import net.imyeyu.blogapi.entity.UserPrivacy;
import net.imyeyu.blogapi.mapper.UserMapper;
import net.imyeyu.blogapi.service.AbstractService;
import net.imyeyu.blogapi.service.UserDataService;
import net.imyeyu.blogapi.service.UserPrivacyService;
import net.imyeyu.blogapi.service.UserService;
import net.imyeyu.blogapi.util.AES;
import net.imyeyu.blogapi.util.Captcha;
import net.imyeyu.blogapi.util.Redis;
import net.imyeyu.blogapi.util.Token;
import net.imyeyu.blogapi.vo.UserSignedIn;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户管理服务实现
 * 
 * <p>夜雨 创建于 2021-02-23 21:43
 */
@Slf4j
@Service
public class UserServiceImplement extends AbstractService implements UserService, BetterJava {

	/** 用户密码盐值 */
	@Value("${setting.salt.password}")
	private String salt;

	@Autowired
	private Token token;

	@Autowired
	private AES aes;

	@Autowired
	private UserDataService dataService;

	@Autowired
	private UserPrivacyService privacyService;

	@Autowired
	private UserMapper mapper;

	@Autowired
	private Redis<Long, String> redisToken;

	/**
	 * 生成密码摘要（与 Token 并不同，Token 用于与前端交流，在 doSignIn 执行成功后才会生成 Token 回推前端）
	 *
	 * @param createdAt 创建时间
	 * @param password  原始密码
	 * @return 密码摘要
	 */
	private String generatePasswordDigest(Long createdAt, String password) throws Exception {
		return Encode.md5(aes.encrypt(createdAt + salt + password));
	}

	@Transactional(rollbackFor = {ServiceException.class, Exception.class})
	@Override
	public UserSignedIn register(User user) throws ServiceException {
		// 校验用户名
		testName(user.getName());
		if (findByName(user.getName()) != null) {
			throw new ServiceException(ReturnCode.DATA_EXIST, "该用户名已存在");
		}
		// 校验密码
		testPassowrd(user.getPassword());
		// 校验邮箱
		if (!ObjectUtils.isEmpty(user.getEmail()) && !StringUtils.isEmpty(user.getEmail().trim())) {
			testEmail(user.getEmail());
		}
		if (findByEmail(user.getEmail()) != null) {
			throw new ServiceException(ReturnCode.DATA_EXIST, "该邮箱已被使用");
		}
		// 明文密码（做自动登录）
		String plainPassword = user.getPassword();
		user.setCreatedAt(System.currentTimeMillis());
		// 摘要密码
		try {
			user.setPassword(generatePasswordDigest(user.getCreatedAt(), user.getPassword()));
		} catch (Exception e) {
			log.error(user.toString());
			e.printStackTrace();
			throw new ServiceException(ReturnCode.ERROR, "编码错误");
		}
		// 注册账号
		create(user);
		// 初始化资料
		dataService.create(new UserData(user.getId()));
		// 初始化隐私控制
		privacyService.create(new UserPrivacy(user.getId()));
		// 清除验证码
		Captcha.clear("REGISTER");
		// 自动登录
		return signIn(String.valueOf(user.getId()), plainPassword);
	}

	@Transactional(rollbackFor = {ServiceException.class, Exception.class})
	@Override
	public UserSignedIn signIn(String user, String password) throws ServiceException {
		User result;
		if (Encode.isNumber(user)) {
			// UID 登录
			result = find(Long.parseLong(user));
		} else if (user.contains("@")) {
			// 邮箱登录
			result = findByEmail(user);
		} else {
			// 用户名登陆
			result = findByName(user);
		}
		if (!ObjectUtils.isEmpty(result)) {
			if (!result.isBanning()) {
				try {
					// 密码摘要校验
					if (result.getPassword().equals(generatePasswordDigest(result.getCreatedAt(), password))) {
						// 生成并缓存 Token
						String token = this.token.generate(result);
						redisToken.set(result.getId(), token, 24);
						// 用户数据
						UserData data = dataService.find(result.getId());
						data.setSignedInIp(getIP());
						data.setSignedInAt(System.currentTimeMillis());
						dataService.update(data);
						result.setData(data);
						Captcha.clear("SIGNIN");
						return result.toToken(token);
					}
				} catch (Exception e) {
					log.error(result.toString());
					e.printStackTrace();
					throw new ServiceException(ReturnCode.ERROR, "编码错误");
				}
				throw new ServiceException(ReturnCode.PARAMS_BAD, "密码错误");
			}
			throw new ServiceException(ReturnCode.RESULT_BAN, "该账号被禁止登录");
		} else {
			throw new ServiceException(ReturnCode.RESULT_NULL, "该用户不存在");
		}
	}

	@Override
	public boolean isSignedIn(String token) throws ServiceException {
		return this.token.isValid(token);
	}

	@Override
	public boolean signOut(String token) throws ServiceException {
		if (isSignedIn(token)) {
			return this.token.clear(token);
		} else {
			throw new ServiceException(ReturnCode.PERMISSION_ERROR, "无权限操作");
		}
	}

	@Override
	public void create(User user) throws ServiceException {
		mapper.create(user);
	}

	@Override
	public User find(Long id) throws ServiceException {
		User user = mapper.find(id);
		if (user != null) {
			return user;
		}
		throw new ServiceException(ReturnCode.RESULT_NULL, "找不到该 UID 用户：" + id);
	}

	@Override
	public User findByName(String name) {
		return mapper.findByName(name);
	}

	@Override
	public User findByEmail(String email) {
		return mapper.findByEmail(email);
	}

	@Transactional(rollbackFor = {ServiceException.class, Exception.class})
	@Override
	public void update(User user) throws ServiceException {
		// 校验用户名
		testName(user.getName());
		User userByName = findByName(user.getName());
		if (userByName != null && !userByName.getId().equals(user.getId())) {
			throw new ServiceException(ReturnCode.DATA_EXIST, "该用户名已存在");
		}
		// 校验邮箱
		if (!StringUtils.isEmpty(user.getEmail().trim())) {
			testEmail(user.getEmail());
		}
		User userByEmail = findByEmail(user.getEmail());
		if (userByEmail != null && !userByEmail.getId().equals(user.getId())) {
			throw new ServiceException(ReturnCode.DATA_EXIST, "该邮箱已被使用");
		}
		user.setUpdatedAt(System.currentTimeMillis());
		mapper.update(user);
	}

	/**
	 * 测试用户名
	 *
	 * @param name 用户名
	 * @throws ServiceException 不通过异常
	 */
	private void testName(String name) throws ServiceException {
		if (StringUtils.isEmpty(name.trim())) {
			throw new ServiceException(ReturnCode.PARAMS_MISS, "请输入用户名");
		} else {
			if (32 < name.length()) {
				throw new ServiceException(ReturnCode.PARAMS_BAD, "用户名长度不可超过 32 位");
			}
			if (name.contains("@")) {
				throw new ServiceException(ReturnCode.PARAMS_BAD, "用户名不能含有 @");
			}
			if (testReg("^[0-9]+.?[0-9]*$", name)) {
				throw new ServiceException(ReturnCode.PARAMS_BAD, "用户名不能是纯数字");
			}
			if (!testReg("^[A-Za-z0-9_\u4e00-\u9fa5]+$", name)) {
				throw new ServiceException(ReturnCode.PARAMS_BAD, "用户名只允许大小写字母、数字、中文");
			}
		}
	}

	/**
	 * 测试密码
	 *
	 * @param passowrd 密码
	 * @throws ServiceException 不通过异常
	 */
	private void testPassowrd(String passowrd) throws ServiceException {
		if (StringUtils.isEmpty(passowrd.trim())) {
			throw new ServiceException(ReturnCode.PARAMS_MISS, "请输入密码");
		} else {
			if (passowrd.length() < 6) {
				throw new ServiceException(ReturnCode.PARAMS_BAD, "密码长度至少需要 6 位");
			}
			if (20 < passowrd.length()) {
				throw new ServiceException(ReturnCode.PARAMS_BAD, "密码长度不可超过 20 位");
			}
			if (!testReg("^[0-9a-zA-Z]*$", passowrd)) {
				throw new ServiceException(ReturnCode.PARAMS_BAD, "密码只允许大小写字母、数字及基础符号");
			}
		}
	}

	/**
	 * 测试邮箱
	 *
	 * @param email 邮箱
	 * @throws ServiceException 不通过异常
	 */
	private void testEmail(String email) throws ServiceException {
		if (!testReg("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email)) {
			throw new ServiceException(ReturnCode.PARAMS_BAD, "请输入正确的邮箱");
		}
	}
}