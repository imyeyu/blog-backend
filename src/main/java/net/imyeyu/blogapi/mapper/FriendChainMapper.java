package net.imyeyu.blogapi.mapper;

import net.imyeyu.blogapi.entity.FriendChain;

import java.util.List;

/**
 * 友链
 *
 * <p>夜雨 创建于 2021-07-15 16:11
 */
public interface FriendChainMapper extends BaseMapper<FriendChain> {

	List<FriendChain> findAll();
}
