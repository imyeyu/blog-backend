package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ServiceException;

import java.util.List;

/**
 * 基本数据库交互接口
 * <p>规范：
 * <ul>
 *     <li>不必实现所有接口。注意！如果不实现而调用将不会执行任何事情</li>
 *     <li>实现类方法顺序：自定义接口（CRUD）、基本交互接口（CRUD）、内部方法</li>
 * </ul>
 *
 * <p>夜雨 创建于 2021-02-23 21:32
 *
 * @param <T> 交互实体
 */
public interface BaseService<T> {

	/**
	 * 创建数据
	 *
	 * @param t 数据对象
	 * @throws ServiceException 服务异常
	 */
	default void create(T t) throws ServiceException {
	}

	/**
	 * 根据 ID 获取对象
	 *
	 * @param id 索引
	 * @return 数据对象
	 * @throws ServiceException 服务异常
	 */
	default T find(Long id) throws ServiceException {
		return null;
	}

	/**
	 * 查询部分
	 *
	 * @param offset 偏移
	 * @param limit  数量
	 * @return 原型类型 List 对象
	 * @throws ServiceException 服务异常
	 */
	default List<T> findMany(Long offset, int limit) throws ServiceException {
		return null;
	}

	/**
	 * 查询所有
	 *
	 * @return 所有数据
	 * @throws ServiceException 服务异常
	 */
	default List<T> findAll() throws ServiceException {
		return null;
	}

	/**
	 * 修改数据
	 *
	 * @param t 数据对象
	 * @throws ServiceException 服务异常
	 */
	default void update(T t) throws ServiceException {
	}

	/**
	 * 软删除
	 *
	 * @param id 索引
	 * @return true 为成功删除
	 * @throws ServiceException 服务异常
	 */
	default boolean delete(Long id) throws ServiceException {
		return false;
	}
}