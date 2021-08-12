package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.UserSetting;

/**
 * 用户设置
 *
 * <p>夜雨 创建于 2021-08-12 16:36
 */
public interface UserSettingMapper extends BaseMapper<UserSetting> {

	UserSetting findByUID(Long uid);
}