package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ServiceException;

import java.util.List;

/**
 * <p>基本数据库交互接口，不必实现所有接口
 * <p>Mapper 继承本接口后不再需要基本 CRUD 接口
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
	default List<T> findMany(long offset, int limit) throws ServiceException {
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
	 * 批量删除
	 *
	 * @param ids 索引数组
	 * @return 成功删除数量
	 * @throws ServiceException 服务异常
	 */
	default Long delete(Long... ids) throws ServiceException {
		return null;
	}
}