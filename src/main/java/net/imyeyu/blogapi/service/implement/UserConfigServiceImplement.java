package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.UserConfig;
import net.imyeyu.blogapi.mapper.UserConfigMapper;
import net.imyeyu.blogapi.service.UserConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户设置服务实现
 *
 * <p>夜雨 创建于 2021-08-12 16:24
 */
@Service
public class UserConfigServiceImplement implements UserConfigService {

	@Autowired
	private UserConfigMapper mapper;

	@Override
	public void create(UserConfig userConfig) throws ServiceException {
		mapper.create(userConfig);
	}

	@Override
	public UserConfig findByUID(Long uid) throws ServiceException {
		return mapper.findByUID(uid);
	}

	@Override
	public void update(UserConfig userConfig) throws ServiceException {
		mapper.update(userConfig);
	}
}