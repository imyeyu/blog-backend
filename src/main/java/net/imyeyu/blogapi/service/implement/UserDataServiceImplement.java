package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ResourceFile;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.UserData;
import net.imyeyu.blogapi.mapper.UserDataMapper;
import net.imyeyu.blogapi.service.FileService;
import net.imyeyu.blogapi.service.UserDataService;
import net.imyeyu.blogapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 用户数据服务实现
 *
 * <p>夜雨 创建于 2021-07-27 17:08
 */
@Service
public class UserDataServiceImplement implements UserDataService {

	/** 头像储存位置 */
	@Value("${setting.paths.user-avatar}")
	private String pathAvatar;

	/** 背景图位置 */
	@Value("${setting.paths.user-wrapper}")
	private String pathWrapper;

	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	@Autowired
	private UserDataMapper mapper;

	@Override
	public void create(UserData userData) throws ServiceException {
		mapper.create(userData);
	}

	@Override
	public UserData find(Long id) throws ServiceException {
		UserData data = mapper.find(id);
		data.setUser(userService.find(id));
		return data;
	}

	@Override
	public void update(UserData userData) throws ServiceException {
		mapper.update(userData);
	}

	@Override
	public String updateAvatar(Long id, MultipartFile file) throws ServiceException {
		try {
			ResourceFile res = new ResourceFile();
			res.setName(id + ".png");
			res.setPath(pathAvatar);
			res.setInputStream(file.getInputStream());
			fileService.upload(res);
			return res.getFullPath();
		} catch (IOException e) {
			throw new ServiceException(ReturnCode.ERROR, "上传文件异常：" + e.getMessage());
		}
	}

	@Override
	public String updateWrapper(Long id, MultipartFile file) throws ServiceException {
		try {
			ResourceFile res = new ResourceFile();
			res.setName(id + ".png");
			res.setPath(pathWrapper);
			res.setInputStream(file.getInputStream());
			fileService.upload(res);
			return res.getFullPath();
		} catch (IOException e) {
			throw new ServiceException(ReturnCode.ERROR, "上传文件异常：" + e.getMessage());
		}
	}
}