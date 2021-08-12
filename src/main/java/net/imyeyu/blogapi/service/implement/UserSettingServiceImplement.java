package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.UserSetting;
import net.imyeyu.blogapi.mapper.UserSettingMapper;
import net.imyeyu.blogapi.service.AbstractService;
import net.imyeyu.blogapi.service.UserSettingService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>夜雨 创建于 2021-08-12 16:24
 */
public class UserSettingServiceImplement extends AbstractService implements UserSettingService {

	@Autowired
	private UserSettingMapper mapper;

	@Override
	public void create(UserSetting userSetting) throws ServiceException {
		mapper.create(userSetting);
	}

	@Override
	public UserSetting findByUID(Long uid) throws ServiceException {
		return mapper.findByUID(uid);
	}

	@Override
	public void update(UserSetting userSetting) throws ServiceException {
		mapper.update(userSetting);
	}
}