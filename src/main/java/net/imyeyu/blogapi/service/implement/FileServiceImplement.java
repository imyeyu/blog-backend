package net.imyeyu.blogapi.service.implement;

import net.imyeyu.betterjava.BetterJava;
import net.imyeyu.betterjava.IO;
import net.imyeyu.blogapi.bean.ResourceFile;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 文件资源服务实现
 *
 * <p>夜雨 创建于 2021-07-31 14:51
 */
@Service
public class FileServiceImplement implements FileService, BetterJava {

	/** 资源文件位置（绝对磁盘目录） */
	@Value("${setting.path}")
	private String resPath;

	@Override
	public void upload(ResourceFile resourceFile) throws ServiceException {
		if (resourceFile.getPath().contains("../")) {
			throw new ServiceException(ReturnCode.REQUEST_BAD, "非法请求：" + resourceFile.getPath());
		}
		File folder = new File(resPath);
		// 资源目录
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				throw new ServiceException(ReturnCode.ERROR, "无法创建资源文件目录");
			}
		}
		try {
			// 文件夹
			File targetFolder = new File(resPath + resourceFile.getPath());
			if (!targetFolder.exists()) {
				if (!targetFolder.mkdirs()) {
					throw new ServiceException(ReturnCode.ERROR, "无法创建目标资源文件目录");
				}
			}
			// 文件
			File targetFile = new File(resPath + resourceFile.getPath() + SEP + resourceFile.getName());
			if (!targetFile.exists()) {
				if (!targetFile.createNewFile()) {
					throw new ServiceException(ReturnCode.ERROR, "无法创建目标资源文件");
				}
			}
			IO.toFile(targetFile, resourceFile.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(ReturnCode.ERROR, "上传文件异常：" + e.getMessage());
		}
	}

	@Override
	public void upload(ResourceFile... file) throws ServiceException {
		for (int i = 0; i < file.length; i++) {
			upload(file[i]);
		}
	}
}