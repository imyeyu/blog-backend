package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.UserData;

/**
 * 用户数据
 *
 * <p>夜雨 创建于 2021-07-27 17:04
 */
public interface UserDataMapper extends BaseMapper<UserData> {

	void updateData(UserData data);
}