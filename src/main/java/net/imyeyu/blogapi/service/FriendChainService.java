package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.FriendChain;

import java.util.List;

/**
 * 友链服务
 *
 * <p>夜雨 创建于 2021-07-15 16:04
 */
public interface FriendChainService extends BaseService<FriendChain> {

	List<FriendChain> findAll() throws ServiceException;
}