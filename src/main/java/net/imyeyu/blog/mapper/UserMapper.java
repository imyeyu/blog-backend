package net.imyeyu.blog.mapper;

import java.util.List;

import net.imyeyu.blog.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * 用户 SQL 映射
 *
 * 夜雨 创建于 2021/2/23 21:33
 */
public interface UserMapper {
	
	Long create(@Param("user") User user);
}