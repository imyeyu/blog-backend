package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.bean.SystemKey;
import net.imyeyu.blogapi.entity.System;

/**
 * 系统配置服务
 *
 * <p>夜雨 创建于 2021-07-20 22:06
 */
public interface SystemService extends BaseService<System> {

	/**
	 * 获取指定类型配置值字符串
	 *
	 * @param key   键
	 * @return 配置值
	 */
	String findByKey(SystemKey key) throws ServiceException;

	/**
	 * 获取指定类型配置值
	 *
	 * @param key   键
	 * @param clazz 强转类型
	 * @param <T> 类型
	 * @return 配置值
	 */
	<T> T findByKey(SystemKey key, Class<T> clazz) throws ServiceException;

	/**
	 * 获取为布尔值
	 *
	 * @param key 键
	 * @return 配置值
	 * @throws ServiceException 服务异常
	 */
	boolean is(SystemKey key) throws ServiceException;

	/**
	 * 获取为布尔值，并取反
	 *
	 * @param key 键
	 * @return 配置值
	 * @throws ServiceException 服务异常
	 */
	boolean not(SystemKey key) throws ServiceException;
}