package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.entity.FriendChain;
import net.imyeyu.blogapi.mapper.FriendChainMapper;
import net.imyeyu.blogapi.service.FriendChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 友链服务实现
 *
 * <p>夜雨 创建于 2021-07-15 16:05
 */
@Service
public class FriendChainServiceImplement implements FriendChainService {

	@Autowired
	private FriendChainMapper mapper;

	@Override
	public List<FriendChain> findAll() {
		return mapper.findAll();
	}
}