package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ResourceFile;
import net.imyeyu.blogapi.bean.ServiceException;

/**
 * 文件资源服务
 *
 * <p>夜雨 创建于 2021-07-31 14:49
 */
public interface FileService {

	void upload(ResourceFile resourceFile) throws ServiceException;

	void upload(ResourceFile... resourceFile) throws ServiceException;
}