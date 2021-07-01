package net.imyeyu.blog.mapper;

import net.imyeyu.blog.entity.DynamicData;
import net.imyeyu.blog.service.BaseService;
import org.apache.ibatis.annotations.Param;

/**
 * 自定义动态数据 SQL 映射
 *
 * 夜雨 创建于 2021-07-01 19:35
 */
public interface DynamicDataMapper extends BaseService<DynamicData> {

	DynamicData findByKey(@Param("key") String key);
}