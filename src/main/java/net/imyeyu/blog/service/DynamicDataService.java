package net.imyeyu.blog.service;

import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.DynamicData;

/**
 * 自定义动态数据服务
 *
 * <p>夜雨 创建于 2021-07-01 19:32
 */
public interface DynamicDataService extends BaseService<DynamicData>{

	DynamicData findByKey(String key) throws ServiceException;
}