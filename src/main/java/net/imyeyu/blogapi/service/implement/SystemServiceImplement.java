package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.bean.SystemKey;
import net.imyeyu.blogapi.entity.System;
import net.imyeyu.blogapi.mapper.SystemMapper;
import net.imyeyu.blogapi.service.SystemService;
import net.imyeyu.blogapi.util.Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * 系统配置服务实现
 *
 * <p>夜雨 创建于 2021-07-20 22:11
 */
@Service
public class SystemServiceImplement implements SystemService {

	@Value("${setting.timeout}")
	private int timeout;

	@Autowired
	private Redis<String, String> redisSetting;

	@Autowired
	private SystemMapper mapper;

	@Override
	public String findByKey(SystemKey key) throws ServiceException {
		String k = key.toString();
		if (redisSetting.has(k)) {
			String value = redisSetting.get(k);
			// 续命
			redisSetting.set(k, value, Duration.ofMinutes(timeout));
			return value;
		} else {
			System setting = mapper.findByKey(k);
			if (timeout != 0) {
				redisSetting.set(k, setting.getValue(), Duration.ofMinutes(timeout));
			}
			return setting.getValue();
		}
	}

	@Override
	public <T> T findByKey(SystemKey key, Class<T> clazz) throws ServiceException {
		try {
			return clazz.cast(findByKey(key));
		} catch (ClassCastException e) {
			e.printStackTrace();
			throw new ServiceException(ReturnCode.ERROR, "系统配置值类型转换异常");
		}
	}

	@Override
	public List<System> findAll() throws ServiceException {
		return mapper.findAll();
	}

	@Override
	public boolean is(SystemKey key) throws ServiceException {
		return Boolean.parseBoolean(findByKey(key));
	}

	@Override
	public boolean not(SystemKey key) throws ServiceException {
		return !is(key);
	}
}