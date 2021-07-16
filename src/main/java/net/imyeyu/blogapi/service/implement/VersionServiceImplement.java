package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.Version;
import net.imyeyu.blogapi.mapper.VersionMapper;
import net.imyeyu.blogapi.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 版本服务实现
 *
 * <p>夜雨 创建于 2021-06-10 16:07
 */
@Service
public class VersionServiceImplement implements VersionService {

	@Autowired
	private VersionMapper mapper;

	@Override
	public Version findByName(String name) throws ServiceException {
		return mapper.findByName(name);
	}
}