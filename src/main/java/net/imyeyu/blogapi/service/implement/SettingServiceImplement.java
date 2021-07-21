package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.bean.SettingKey;
import net.imyeyu.blogapi.entity.Setting;
import net.imyeyu.blogapi.mapper.SettingMapper;
import net.imyeyu.blogapi.service.SettingService;
import net.imyeyu.blogapi.util.Redis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * 系统配置服务实现
 *
 * <p>夜雨 创建于 2021-07-20 22:11
 */
@Service
public class SettingServiceImplement implements SettingService {

	@Value("${setting.timeout}")
	private int timeout;

	@Autowired
	private Redis<String, String> redisSetting;

	@Autowired
	private SettingMapper mapper;

	@Override
	public String findByKey(SettingKey key) throws ServiceException {
		String k = key.toString();
		if (redisSetting.has(k)) {
			return redisSetting.get(k);
		} else {
			Setting setting = mapper.findByKey(k);
			if (timeout != 0) {
				redisSetting.set(k, setting.getValue(), Duration.ofMinutes(timeout));
			}
			return setting.getValue();
		}
	}

	@Override
	public <T> T findByKey(SettingKey key, Class<T> clazz) throws ServiceException {
		try {
			return clazz.cast(findByKey(key));
		} catch (ClassCastException e) {
			e.printStackTrace();
			throw new ServiceException(ReturnCode.ERROR, "系统配置值类型转换异常");
		}
	}

	@Override
	public boolean is(SettingKey key) throws ServiceException {
		return Boolean.parseBoolean(findByKey(key));
	}

	@Override
	public boolean not(SettingKey key) throws ServiceException {
		return !is(key);
	}
}