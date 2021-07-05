package net.imyeyu.blog.service;

import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.Version;

/**
 * 版本管理
 *
 * <p>夜雨 创建于 2021-06-10 16:06
 */
public interface VersionService extends BaseService<Version> {

	Version findByName(String name) throws ServiceException;
}