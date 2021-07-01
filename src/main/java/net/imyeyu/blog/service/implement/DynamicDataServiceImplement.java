package net.imyeyu.blog.service.implement;

import net.imyeyu.blog.bean.ServiceException;
import net.imyeyu.blog.entity.DynamicData;
import net.imyeyu.blog.mapper.DynamicDataMapper;
import net.imyeyu.blog.service.DynamicDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 夜雨 创建于 2021-07-01 19:41
 */
@Service
public class DynamicDataServiceImplement implements DynamicDataService {

	@Autowired
	private DynamicDataMapper mapper;

	@Override
	public void create(DynamicData dynamicData) throws ServiceException {

	}

	@Override
	public DynamicData find(Long id) throws ServiceException {
		return null;
	}

	@Override
	public List<DynamicData> findMany(long offset, int limit) throws ServiceException {
		return null;
	}

	@Override
	public void update(DynamicData dynamicData) {

	}

	@Override
	public Long delete(Long... ids) throws ServiceException {
		return null;
	}

	@Override
	public DynamicData findByKey(String key) throws ServiceException {
		return mapper.findByKey(key);
	}
}