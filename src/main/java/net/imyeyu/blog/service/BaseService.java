package net.imyeyu.blog.service;

import java.util.List;

/**
 * 基本数据库交互接口
 * 
 * @param <T> 交互实体
 *
 * 夜雨 创建于 2021-02-23 21:32
 */
public interface BaseService<T> {

	/** @param t 创建数据 */
	void create(T t);

	/**
	 * 根据 ID 获取对象
	 *
	 * @param id 索引
	 * @return   原型对象
	 */
	T find(Long id);

	/**
	 * 查询部分
	 * 
	 * @param offset 偏移
	 * @param limit  数量
	 * @return 原型类型 List 对象
	 */
	List<T> find(long offset, int limit);
	
	/** @param t  修改数据 */
	void update(T t);

	/**
	 * 批量删除
	 * 
	 * @param ids 索引数组
	 * @return    成功删除数量
	 */
	Long delete(Long... ids);
}