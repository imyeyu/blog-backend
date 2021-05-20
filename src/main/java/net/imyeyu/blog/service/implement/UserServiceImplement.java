package net.imyeyu.blog.service.implement;

import java.util.List;

import net.imyeyu.betterjava.Encode;
import net.imyeyu.blog.entity.User;
import net.imyeyu.blog.mapper.UserMapper;
import net.imyeyu.blog.service.UserService;
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
	public void create(User user) {
		user.setCreatedAt(System.currentTimeMillis());
		// 加盐摘要
		user.setPassword(Encode.md5(user.getCreatedAt() + "Nagiasu" + user.getPassword()));
		mapper.create(user);
	}

	@Override
	public User find(Long id) {
		return null;
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
