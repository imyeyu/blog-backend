package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.Version;

/**
 * 版本管理
 *
 * <p>夜雨 创建于 2021-06-10 16:06
 */
public interface VersionService extends BaseService<Version> {

	Version findByName(String name) throws ServiceException;
}