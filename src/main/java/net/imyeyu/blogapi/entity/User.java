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
	private Long canceledAt;

	private UserData data;

	public UserSignedIn toToken(String token) {
		UserSignedIn usi = new UserSignedIn(name, token);
		usi.setId(id);
		usi.setCreatedAt(System.currentTimeMillis());
		return usi;
	}
}