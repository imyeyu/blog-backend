package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.User;
import net.imyeyu.blogapi.entity.UserData;
import org.apache.ibatis.annotations.Param;

/**
 * 用户
 *
 * <p>夜雨 创建于 2021-02-23 21:33
 */
public interface UserMapper extends BaseMapper<User> {

	User findByName(@Param("name") String name);

	User findByEmail(@Param("email") String email);

	void createData(UserData userData);

	UserData findData(Long userId);

	void updateData(UserData userData);

	void deleteData(Long userId);
}