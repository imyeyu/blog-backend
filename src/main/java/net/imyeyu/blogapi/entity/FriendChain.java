package net.imyeyu.blogapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 夜雨 创建于 2021-07-15 15:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FriendChain extends BaseEntity implements Serializable {

	private String icon;
	private String name;
	private String link;
}