package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.UserSettings;

/**
 * 用户设置服务
 *
 * <p>夜雨 创建于 2021-08-12 16:23
 */
public interface UserSettingsService extends BaseService<UserSettings> {

	/**
	 * 根据用户 ID 获取用户设置
	 *
	 * @param uid 用户 ID
	 * @return 用户设置
	 * @throws ServiceException 服务异常
	 */
	UserSettings findByUID(Long uid) throws ServiceException;
}