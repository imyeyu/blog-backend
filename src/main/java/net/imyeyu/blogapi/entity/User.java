package net.imyeyu.blogapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.imyeyu.blogapi.vo.UserSignedIn;

import java.io.Serializable;

/**
 * 用户
 *
 * <p>夜雨 创建于 2021-03-01 17:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements Serializable {

	private String email;
	private String name;
	private String password;
	private Long unmuteAt;
	private Long unbanAt;

	private UserData data;

	/** @return true 为禁言中 */
	public boolean isMuting() {
		return unmuteAt != null && System.currentTimeMillis() < unmuteAt;
	}

	/** @return true 为封禁中 */
	public boolean isBanning() {
		return unbanAt != null && System.currentTimeMillis() < unbanAt;
	}

	public UserSignedIn toToken(String token) {
		UserSignedIn usi = new UserSignedIn(name, token, data);
		usi.setId(id);
		usi.setCreatedAt(System.currentTimeMillis());
		return usi;
	}
}