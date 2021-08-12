package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.UserPrivacy;

/**
 * 用户隐私控制
 *
 * <p>夜雨 创建于 2021-07-27 17:18
 */
public interface UserPrivacyMapper extends BaseMapper<UserPrivacy> {

	UserPrivacy findByUID(Long uid);
}