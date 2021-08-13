package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.UserSettings;
import net.imyeyu.blogapi.mapper.UserSettingsMapper;
import net.imyeyu.blogapi.service.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户设置服务实现
 *
 * <p>夜雨 创建于 2021-08-12 16:24
 */
@Service
public class UserSettingsServiceImplement implements UserSettingsService {

	@Autowired
	private UserSettingsMapper mapper;

	@Override
	public void create(UserSettings userSettings) throws ServiceException {
		mapper.create(userSettings);
	}

	@Override
	public UserSettings findByUID(Long uid) throws ServiceException {
		return mapper.findByUID(uid);
	}

	@Override
	public void update(UserSettings userSettings) throws ServiceException {
		mapper.update(userSettings);
	}
}