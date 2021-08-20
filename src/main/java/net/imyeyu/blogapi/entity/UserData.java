package net.imyeyu.blogapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.imyeyu.blogapi.annotation.Entity;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;

/**
 * 用户数据
 *
 * <p>夜雨 创建于 2021-05-29 15:58
 */
@Data
@Entity
@NoArgsConstructor
public class UserData implements Serializable {

	@Transient
	private transient static UserService userService;

	private Long id;

	private Long userId;
	private Boolean hasWrapper;
	private Boolean hasAvatar;
	private Integer exp;
	private Byte sex;
	private Long birth;
	private String qq;
	private String sign;
	private String signedInIp;
	private Long signedInAt;

	private Long updatedAt;

	private User user;

	@Autowired
	public UserData(UserService userService) {
		UserData.userService = userService;
	}

	public UserData(Long userId) {
		this.userId = userId;
	}

	/**
	 * 携带账号数据
	 *
	 * @return 本实体
	 * @throws ServiceException 服务异常
	 */
	public UserData withUser() throws ServiceException {
		user = userService.find(userId);
		return this;
	}

	/**
	 * 根据隐私控制过滤数据（暂定方案）
	 *
	 * @param privacy 隐私控制数据
	 * @return 用户数据
	 */
	public UserData filterPrivacy(UserPrivacy privacy) {
		if (!privacy.isSex())        sex        = null;
		if (!privacy.isBirth())      birth      = null;
		if (!privacy.isQq())         qq         = null;
		if (!privacy.isEmail())      user.setEmail(null);
		if (!privacy.isSignedInAt()) signedInAt = null;
		if (!privacy.isCreatedAt())  user.setCreatedAt(null);
		return this;
	}
}