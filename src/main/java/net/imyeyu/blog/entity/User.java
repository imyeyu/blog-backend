package net.imyeyu.blog.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.imyeyu.blog.vo.UserVO;

import java.io.Serializable;

/**
 * 用户
 *
 * 夜雨 创建于 2021/3/1 17:11
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements Serializable {

	private String email;
	private String name;
	private String password;
	private Long canceledAt;

	public UserVO toVO(String token) {
		return new UserVO(id, email, name, token);
	}
}