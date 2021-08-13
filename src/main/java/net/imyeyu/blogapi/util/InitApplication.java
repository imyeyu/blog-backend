package net.imyeyu.blogapi.util;

import lombok.extern.slf4j.Slf4j;
import net.imyeyu.blogapi.entity.Settings;
import net.imyeyu.blogapi.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * SpringBoot 启动事件，主要输出基本参数，避免混淆运行环境
 *
 * <p>夜雨 创建于 2021-07-24 14:29
 */
@Slf4j
@Component
public class InitApplication implements ApplicationRunner {

	@Value("${setting.path}")
	private String resPath;

	@Value("${spring.datasource.url}")
	private String jdbcURL;

	@Value("${spring.redis.host}")
	private String redisURL;

	@Value("${spring.redis.port}")
	private int redisPort;

	@Autowired
	private SettingsService settingsService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		List<Settings> settings = settingsService.findAll();
		log.info("JDBC URL: " + jdbcURL);
		log.info("Redis URL: " + redisURL + ':' + redisPort);
		log.info("Resources Path: " + new File(resPath).getAbsolutePath());
		log.info("System Settings:");
		for (Settings setting : settings) {
			log.info("\t" + setting.getKey() + "\t: " + setting.getValue());
		}
		log.info("Init Application Finished.");
	}
}