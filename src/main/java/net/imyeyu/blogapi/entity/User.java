package net.imyeyu.blogapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.imyeyu.blogapi.annotation.Entity;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.service.UserConfigService;
import net.imyeyu.blogapi.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;

/**
 * 用户
 *
 * <p>夜雨 创建于 2021-03-01 17:11
 */
@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements Serializable {

	@Transient
	private transient static UserDataService dataService;

	@Transient
	private transient static UserConfigService configService;

	private String email;
	private String name;
	private String password;
	private Long unmuteAt;
	private Long unbanAt;

	private UserData data;
	private UserConfig config;

	@Autowired
	public User(UserDataService dataService, UserConfigService configService) {
		User.dataService = dataService;
		User.configService = configService;
	}

	/** @return true 为禁言中 */
	public boolean isMuting() {
		return unmuteAt != null && System.currentTimeMillis() < unmuteAt;
	}

	/** @return true 为封禁中 */
	public boolean isBanning() {
		return unbanAt != null && System.currentTimeMillis() < unbanAt;
	}

	/**
	 * 携带用户数据
	 *
	 * @return 本实体
	 * @throws ServiceException 服务异常
	 */
	public User withData() throws ServiceException {
		data = dataService.findByUID(getId());
		return this;
	}

	/**
	 * 携带用户设置
	 *
	 * @return 本实体
	 * @throws ServiceException 服务异常
	 */
	public User withConfig() throws ServiceException {
		config = configService.findByUID(getId());
		return this;
	}
}