package net.imyeyu.blogapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.imyeyu.blogapi.bean.SettingKey;

import java.io.Serializable;

/**
 * 系统配置
 *
 * <p>夜雨 创建于 2021-07-20 21:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Setting extends BaseEntity implements Serializable {

	private SettingKey key;
	private String value;
}