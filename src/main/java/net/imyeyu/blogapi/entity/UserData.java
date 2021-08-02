package net.imyeyu.blogapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户数据
 *
 * <p>夜雨 创建于 2021-05-29 15:58
 */
@Data
@NoArgsConstructor
public class UserData implements Serializable {

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

	public UserData(Long userId) {
		this.userId = userId;
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