package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.UserSettings;

/**
 * 用户设置
 *
 * <p>夜雨 创建于 2021-08-12 16:36
 */
public interface UserSettingsMapper extends BaseMapper<UserSettings> {

	UserSettings findByUID(Long uid);
}