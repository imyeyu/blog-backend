package net.imyeyu.blogapi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.imyeyu.blogapi.entity.BaseEntity;

/**
 * 用户令牌
 *
 * <p>夜雨 创建于 2021-05-25 23:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserSignedIn extends BaseEntity {

	private String name;
	private String token;
}