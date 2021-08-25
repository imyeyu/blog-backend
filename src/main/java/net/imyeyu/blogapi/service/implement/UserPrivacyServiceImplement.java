package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.UserPrivacy;
import net.imyeyu.blogapi.mapper.UserPrivacyMapper;
import net.imyeyu.blogapi.service.UserPrivacyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户隐私控制服务实现
 *
 * <p>夜雨 创建于 2021-07-27 17:19
 */
@Service
public class UserPrivacyServiceImplement implements UserPrivacyService {

	@Autowired
	private UserPrivacyMapper mapper;

	@Override
	public void create(UserPrivacy userPrivacy) throws ServiceException {
		mapper.create(userPrivacy);
	}

	@Override
	public UserPrivacy findByUID(Long uid) throws ServiceException {
		return mapper.findByUID(uid);
	}

	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
	@Override
	public void update(UserPrivacy userPrivacy) throws ServiceException {
		mapper.update(userPrivacy);
	}
}