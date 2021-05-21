package net.imyeyu.blog.service.implement;

import java.util.List;

import net.imyeyu.betterjava.Encode;
import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.User;
import net.imyeyu.blog.mapper.UserMapper;
import net.imyeyu.blog.service.UserService;
import net.imyeyu.blog.util.Token;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户管理
 * 
 * 夜雨 创建于 2021/2/23 21:43
 */
@Service
public class UserServiceImplement implements UserService {
	
	@Autowired
    private UserMapper mapper;

	@Override
	public boolean doLogin(String user, String password) throws ServiceException {
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
			String token = Token.generate(result);
			// 令牌校验
			boolean isValid = new Token(token).isValid(result.getId());
			if (isValid) {
				// 登录成功
				return true;
			} else {
				throw new ServiceException("密码错误");
			}
		} else {
			throw new ServiceException("用户不存在");
		}
	}

	@Override
	public void create(User user) {
		user.setCreatedAt(System.currentTimeMillis());
		// 加盐摘要
		user.setPassword(Encode.md5(user.getCreatedAt() + "Nagiasu" + user.getPassword()));
		mapper.create(user);
	}

	@Override
	public User find(Long id) {
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
	public List<User> find(long offset, int limit) {
		return null;
	}

	@Override
	public void update(User user) {
		// 更新密码
		user.setPassword(Encode.md5(user.getCreatedAt() + "Nagiasu" + user.getPassword()));
	}

	@Override
	public Long delete(Long... ids) {
		return null;
	}
}
