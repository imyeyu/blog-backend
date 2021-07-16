package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.DynamicData;
import org.apache.ibatis.annotations.Param;

/**
 * 自定义动态数据
 *
 * <p>夜雨 创建于 2021-07-01 19:35
 */
public interface DynamicDataMapper extends BaseMapper<DynamicData> {

	DynamicData findByKey(@Param("key") String key);
}