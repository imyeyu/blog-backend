package net.imyeyu.blogapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户数据
 *
 * <p>夜雨 创建于 2021-05-29 15:58
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserData extends BaseEntity implements Serializable {

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

	private User user;

	public UserData(Long userId) {
		this.userId = userId;
	}
}