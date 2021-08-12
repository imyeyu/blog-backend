package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.UserPrivacy;

/**
 * 用户隐私控制服务
 *
 * <p>夜雨 创建于 2021-07-27 17:18
 */
public interface UserPrivacyService extends BaseService<UserPrivacy> {

	/**
	 * 根据用户 ID 获取用户隐私控制
	 *
	 * @param uid 用户 ID
	 * @return 用户隐私控制
	 * @throws ServiceException 服务异常
	 */
	UserPrivacy findByUID(Long uid) throws ServiceException;
}