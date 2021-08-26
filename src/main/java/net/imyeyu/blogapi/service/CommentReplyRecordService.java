package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.CommentReplyRecord;
import net.imyeyu.blogapi.entity.UserComment;

import java.util.List;

/**
 * 评论回复记录服务
 *
 * <p>夜雨 创建于 2021-08-25 14:35
 */
public interface CommentReplyRecordService extends BaseService<CommentReplyRecord> {

	/**
	 * 根据用户 ID 获取
	 *
	 * @param uid    用户 ID
	 * @param offset 偏移
	 * @param limit  数量
	 * @return 回复记录
	 * @throws ServiceException 服务异常
	 */
	List<UserComment> findManyByUID(Long uid, Long offset, int limit) throws ServiceException;

	/**
	 * 删除记录（非软删除）
	 *
	 * @param rid 回复 ID
	 * @throws ServiceException 服务异常
	 */
	void deleteByRID(Long rid) throws ServiceException;

	/**
	 * 删除记录（非软删除）
	 *
	 * @param uid 用户 ID
	 * @param rid 回复 ID
	 * @throws ServiceException 服务异常
	 */
	void deleteByRIDwithUID(Long uid, Long rid) throws ServiceException;
}