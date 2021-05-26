package net.imyeyu.blog.service.implement;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import net.imyeyu.betterjava.Encode;
import net.imyeyu.blog.bean.ReturnCode;
import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.User;
import net.imyeyu.blog.mapper.UserMapper;
import net.imyeyu.blog.service.UserService;
import net.imyeyu.blog.util.Token;
import net.imyeyu.blog.vo.UserVO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>用户管理
 * 
 * 夜雨 创建于 2021/2/23 21:43
 */
@Slf4j
@Service
public class UserServiceImplement implements UserService {
	
	@Autowired
    private UserMapper mapper;

	@Autowired
	private Token token;

	/**
	 * <p>生成密码摘要（与 Token 并不同，Token 用于与前端交流，在 doSignIn 执行成功后才会生成 Token 回推前端）
	 * <p>加盐《天使恋曲》
	 *
	 * @param name      用户名
	 * @param createdAt 创建时间
	 * @param password  原始密码
	 * @return 密码摘要
	 */
	private String generatePasswordDigest(String name, Long createdAt, String password) {
		return Encode.md5(name + createdAt + "AngelicSerenade" + password);
	}

	@Override
	public UserVO doSignIn(String user, String password) throws ServiceException {
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
				token.isValid(result.getId(), token.generate(result));
				return result.toVO(token.generate(result));
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

	/**
	 * 创建用户，此时的 User password 是明文
	 *
	 * @param user 用户
	 */
	@Override
	public void create(User user) {
		user.setCreatedAt(System.currentTimeMillis());
		// 摘要密码
		user.setPassword(generatePasswordDigest(user.getName(), user.getCreatedAt(), user.getPassword()));
		mapper.create(user);
	}

	@Override
	public User find(Long id) {
		return mapper.findById(id);
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
	public List<User> find(long offset, int limit) {
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
}
