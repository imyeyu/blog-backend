package net.imyeyu.blogapi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户隐私控制
 *
 * <p>夜雨 创建于 2021-07-27 16:51
 */
@Data
@NoArgsConstructor
public class UserPrivacy implements Serializable {

	private Long id;
	private Long userId;

	private boolean email;
	private boolean sex;
	private boolean birth;
	private boolean qq;
	private boolean sign;
	private boolean comment;
	private boolean signedInAt;
	private boolean createdAt;

	private Long updatedAt;

	public UserPrivacy(Long userId) {
		this.userId = userId;
	}
}