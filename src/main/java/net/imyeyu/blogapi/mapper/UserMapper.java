package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.User;
import net.imyeyu.blogapi.entity.UserComment;

import java.util.List;

/**
 * 用户
 *
 * <p>夜雨 创建于 2021-02-23 21:33
 */
public interface UserMapper extends BaseMapper<User> {

	User findByName(String name);

	User findByEmail(String email);

	void updatePassword(Long id, String password);

	List<UserComment> findManyUserComment(Long uid, Long offset, int limit);
}