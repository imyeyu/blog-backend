package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.Version;
import net.imyeyu.blogapi.service.BaseService;
import org.apache.ibatis.annotations.Param;

/**
 * 版本管理
 *
 * <p>夜雨 创建于 2021-06-10 16:08
 */
public interface VersionMapper extends BaseService<Version> {

	Version findByName(@Param("name") String name);
}