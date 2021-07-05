package net.imyeyu.blog.mapper;

import net.imyeyu.blog.entity.User;
import net.imyeyu.blog.entity.UserData;
import net.imyeyu.blog.service.BaseService;
import org.apache.ibatis.annotations.Param;

/**
 * 用户
 *
 * 夜雨 创建于 2021/2/23 21:33
 */
public interface UserMapper extends BaseService<User> {

	User findByName(@Param("name") String name);

	User findByEmail(@Param("email") String email);

	void createData(UserData userData);

	UserData findData(Long userId);

	void updateData(UserData userData);

	void deleteData(Long userId);
}