package net.imyeyu.blogapi.bean;

import lombok.Data;

import java.io.File;
import java.io.InputStream;

/**
 * 资源文件
 *
 * <p>夜雨 创建于 2021-07-31 15:31
 */
@Data
public class ResourceFile {

	private String path;
	private String name;

	private InputStream inputStream;

	public String getFullPath() {
		return path + "/" + name;
	}
}