package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.Settings;

/**
 * 系统配置
 *
 * <p>夜雨 创建于 2021-07-20 22:26
 */
public interface SettingsMapper extends BaseMapper<Settings> {

	Settings findByKey(String key);
}