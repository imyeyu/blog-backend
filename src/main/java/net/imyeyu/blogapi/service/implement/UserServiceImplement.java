package net.imyeyu.blogapi.service.implement;

import lombok.extern.slf4j.Slf4j;
import net.imyeyu.betterjava.Encode;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.User;
import net.imyeyu.blogapi.entity.UserData;
import net.imyeyu.blogapi.mapper.UserMapper;
import net.imyeyu.blogapi.service.UserService;
import net.imyeyu.blogapi.util.Redis;
import net.imyeyu.blogapi.util.Token;
import net.imyeyu.blogapi.vo.UserSignedIn;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>用户管理
 * 
 * <p>夜雨 创建于 2021-02-23 21:43
 */
@Slf4j
@Service
public class UserServiceImplement implements UserService {

	/** 用户密码盐值 */
	@Value("${slat.user.password}")
	private String salt;

	@Autowired
	private UserMapper mapper;

	@Autowired
	private Redis<Long, String> redisToken;

	@Autowired
	private Token token;

	/**
	 * 生成密码摘要（与 Token 并不同，Token 用于与前端交流，在 doSignIn 执行成功后才会生成 Token 回推前端）
	 *
	 * @param name      用户名
	 * @param createdAt 创建时间
	 * @param password  原始密码
	 * @return 密码摘要
	 */
	private String generatePasswordDigest(String name, Long createdAt, String password) {
		return Encode.md5(name + createdAt + salt + password);
	}

	@Transactional(rollbackFor = ServiceException.class)
	@Override
	public UserSignedIn register(User user) throws ServiceException {
		if (findByName(user.getName()) != null) {
			throw new ServiceException(ReturnCode.DATA_CONFLICT, "该用户名已存在");
		}
		if (findByEmail(user.getEmail()) != null) {
			throw new ServiceException(ReturnCode.DATA_CONFLICT, "该邮箱已被使用");
		}
		// 明文密码（做自动登录）
		String plainPassword = user.getPassword();
		user.setCreatedAt(System.currentTimeMillis());
		// 摘要密码
		user.setPassword(generatePasswordDigest(user.getName(), user.getCreatedAt(), user.getPassword()));
		// 注册账号
		create(user);
		// 创建资料
		createData(new UserData(user.getId()));
		// 自动登录
		return signIn(String.valueOf(user.getId()), plainPassword);
	}

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
			// 密码摘要校验
			if (result.getPassword().equals(generatePasswordDigest(result.getName(), result.getCreatedAt(), password))) {
				// 生成并缓存 Token
				String token = this.token.generate(result);
				redisToken.set(result.getId(), token, 24);
				return result.toToken(token);
			}
			throw new ServiceException(ReturnCode.PARAMS_BAD, "密码错误");
		} else {
			throw new ServiceException(ReturnCode.RESULT_NULL, "用户不存在");
		}
	}

	@Override
	public boolean isSignedIn(Long uid, String token) throws ServiceException {
		return this.token.isValid(uid, token);
	}

	@Override
	public boolean signOut(Long uid, String token) throws ServiceException {
		if (isSignedIn(uid, token)) {
			return this.token.clear(uid);
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
		return mapper.find(id);
	}

	@Override
	public User findByName(String name) {
		return mapper.findByName(name);
	}

	@Override
	public User findByEmail(String email) {
		return mapper.findByEmail(email);
	}

	@Override
	public List<User> findMany(long offset, int limit) {
		return null;
	}

	@Override
	public void update(User user) {
		// 更新用户
		user.setPassword(Encode.md5(user.getCreatedAt() + "Nagiasu" + user.getPassword()));
	}

	@Override
	public Long delete(Long... ids) {
		return null;
	}
	
	@Override
	public void createData(UserData userData) {
		mapper.createData(userData);
	}

	@Override
	public UserData findData(Long userId) throws ServiceException {
		UserData result = mapper.findData(userId);
		if (result != null) {
			return mapper.findData(userId);
		} else {
			new Exception("找不到该用户数据，UID = " + userId).printStackTrace();
			throw new ServiceException(ReturnCode.RESULT_NULL, "找不到该用户数据，UID = " + userId);
		}
	}

	@Override
	public void updateData(UserData userData) {
		mapper.updateData(userData);
	}

	@Override
	public void deleteData(Long userId) {
	}
}
