package net.imyeyu.blog.mapper;

import net.imyeyu.blog.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * 用户 SQL 映射
 *
 * 夜雨 创建于 2021/2/23 21:33
 */
public interface UserMapper {

	User find(@Param("id") Long id);

	User findByName(@Param("name") String name);

	User findByEmail(@Param("email") String email);
	
	Long create(@Param("user") User user);
}