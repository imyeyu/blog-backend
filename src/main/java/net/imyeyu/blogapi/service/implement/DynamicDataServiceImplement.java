package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.DynamicData;
import net.imyeyu.blogapi.mapper.DynamicDataMapper;
import net.imyeyu.blogapi.service.DynamicDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>夜雨 创建于 2021-07-01 19:41
 */
@Service
public class DynamicDataServiceImplement implements DynamicDataService {

	@Autowired
	private DynamicDataMapper mapper;

	@Override
	public DynamicData findByKey(String key) throws ServiceException {
		return mapper.findByKey(key);
	}
}