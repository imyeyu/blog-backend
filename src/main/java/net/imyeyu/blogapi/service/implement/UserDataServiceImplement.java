package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.UserData;
import net.imyeyu.blogapi.mapper.UserDataMapper;
import net.imyeyu.blogapi.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户数据服务实现
 *
 * <p>夜雨 创建于 2021-07-27 17:08
 */
@Service
public class UserDataServiceImplement implements UserDataService {

	@Autowired
	private UserDataMapper mapper;

	@Override
	public void create(UserData userData) throws ServiceException {
		mapper.create(userData);
	}

	@Override
	public UserData find(Long id) throws ServiceException {
		return mapper.find(id);
	}

	@Override
	public void update(UserData userData) throws ServiceException {
		mapper.update(userData);
	}
}