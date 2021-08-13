package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.bean.SettingsKey;
import net.imyeyu.blogapi.entity.Settings;
import net.imyeyu.blogapi.mapper.SettingsMapper;
import net.imyeyu.blogapi.service.SettingsService;
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
public class SettingsServiceImplement implements SettingsService {

	@Value("${setting.timeout}")
	private int timeout;

	@Autowired
	private Redis<String, String> redisSetting;

	@Autowired
	private SettingsMapper mapper;

	@Override
	public String findByKey(SettingsKey key) throws ServiceException {
		String k = key.toString();
		if (redisSetting.has(k)) {
			String value = redisSetting.get(k);
			// 续命
			redisSetting.set(k, value, Duration.ofMinutes(timeout));
			return value;
		} else {
			Settings setting = mapper.findByKey(k);
			if (timeout != 0) {
				redisSetting.set(k, setting.getValue(), Duration.ofMinutes(timeout));
			}
			return setting.getValue();
		}
	}

	@Override
	public <T> T findByKey(SettingsKey key, Class<T> clazz) throws ServiceException {
		try {
			return clazz.cast(findByKey(key));
		} catch (ClassCastException e) {
			e.printStackTrace();
			throw new ServiceException(ReturnCode.ERROR, "系统配置值类型转换异常");
		}
	}

	@Override
	public List<Settings> findAll() throws ServiceException {
		return mapper.findAll();
	}

	@Override
	public boolean is(SettingsKey key) throws ServiceException {
		return Boolean.parseBoolean(findByKey(key));
	}

	@Override
	public boolean not(SettingsKey key) throws ServiceException {
		return !is(key);
	}
}