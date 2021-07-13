package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.Version;
import net.imyeyu.blogapi.mapper.VersionMapper;
import net.imyeyu.blogapi.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>夜雨 创建于 2021-06-10 16:07
 */
@Service
public class VersionServiceImplement implements VersionService {

	@Autowired
	private VersionMapper mapper;

	@Override
	public void create(Version version) throws ServiceException {
		mapper.create(version);
	}

	@Override
	public Version find(Long id) throws ServiceException {
		return mapper.find(id);
	}

	@Override
	public Version findByName(String name) throws ServiceException {
		return mapper.findByName(name);
	}

	@Override
	public List<Version> findMany(long offset, int limit) throws ServiceException {
		return mapper.findMany(offset, limit);
	}

	@Override
	public void update(Version version) {
		mapper.update(version);
	}

	@Override
	public Long delete(Long... ids) throws ServiceException {
		return mapper.delete(ids);
	}
}