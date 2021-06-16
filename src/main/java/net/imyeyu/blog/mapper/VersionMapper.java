package net.imyeyu.blog.mapper;

import net.imyeyu.blog.entity.Version;
import net.imyeyu.blog.service.BaseService;
import org.apache.ibatis.annotations.Param;

/**
 * 版本管理 SQL 映射
 *
 * 夜雨 创建于 2021-06-10 16:08
 */
public interface VersionMapper extends BaseService<Version> {

	Version findByName(@Param("name") String name);
}