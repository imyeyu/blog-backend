package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.service.BaseService;

import java.util.List;

/**
 * 基本 SQL 映射
 *
 * <p>夜雨 创建于 2021-07-16 09:40
 */
public interface BaseMapper<T> extends BaseService<T> {

	/**
	 * 创建数据
	 *
	 * @param t 数据对象
	 */
	void create(T t);

	/**
	 * 根据 ID 获取对象
	 *
	 * @param id 索引
	 * @return 数据对象
	 */
	T find(Long id);

	/**
	 * 查询部分
	 *
	 * @param offset 偏移
	 * @param limit  数量
	 * @return 原型类型 List 对象
	 */
	List<T> findMany(Long offset, int limit);

	/**
	 * 修改数据
	 *
	 * @param t 数据对象
	 */
	void update(T t);

	/**
	 * 批量删除
	 *
	 * @param ids 索引数组
	 * @return 成功删除数量
	 */
	Long delete(Long... ids);
}
