package net.imyeyu.blogapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 夜雨 创建于 2021-07-15 15:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FriendChain extends BaseEntity {

	private String icon;
	private String name;
	private String link;
}