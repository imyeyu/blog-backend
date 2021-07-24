package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.Setting;

/**
 * 系统配置
 *
 * <p>夜雨 创建于 2021-07-20 22:26
 */
public interface SettingMapper extends BaseMapper<Setting> {

	Setting findByKey(String key);
}