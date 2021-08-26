package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ResourceFile;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.User;
import net.imyeyu.blogapi.entity.UserData;
import net.imyeyu.blogapi.mapper.UserDataMapper;
import net.imyeyu.blogapi.service.FileService;
import net.imyeyu.blogapi.service.UserDataService;
import net.imyeyu.blogapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 用户数据服务实现
 *
 * <p>夜雨 创建于 2021-07-27 17:08
 */
@Service("dataService")
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
	public UserData findByUID(Long uid) throws ServiceException {
		UserData userData = mapper.findByUID(uid);
		if (userData != null) {
			return userData;
		}
		throw new ServiceException(ReturnCode.RESULT_NULL, "找不到该 UID 用户：" + uid);
	}

	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
	@Override
	public void updateData(UserData data) throws ServiceException {
		User user = userService.find(data.getUserId());
		if (!user.getEmail().equals(data.getUser().getEmail())) {
			// 修改了邮箱，强制重新验证
			data.getUser().setEmailVerify(false);
		}
		data.setUpdatedAt(System.currentTimeMillis());
		mapper.updateData(data);
	}

	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
	@Override
	public String updateAvatar(Long id, MultipartFile file) throws ServiceException {
		try {
			// 处理文件
			ResourceFile res = new ResourceFile();
			res.setName(id + ".png");
			res.setPath(pathAvatar);
			res.setInputStream(file.getInputStream());
			fileService.upload(res);
			// 更新数据库
			UserData data = find(id);
			data.setHasAvatar(true);
			mapper.update(data);
			return res.getFullPath();
		} catch (IOException e) {
			throw new ServiceException(ReturnCode.ERROR, "上传文件异常：" + e.getMessage());
		}
	}

	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
	@Override
	public String updateWrapper(Long id, MultipartFile file) throws ServiceException {
		try {
			// 处理文件
			ResourceFile res = new ResourceFile();
			res.setName(id + ".png");
			res.setPath(pathWrapper);
			res.setInputStream(file.getInputStream());
			fileService.upload(res);
			// 更新数据库
			UserData data = find(id);
			data.setHasWrapper(true);
			mapper.update(data);
			return res.getFullPath();
		} catch (IOException e) {
			throw new ServiceException(ReturnCode.ERROR, "上传文件异常：" + e.getMessage());
		}
	}

	@Override
	public void create(UserData userData) throws ServiceException {
		mapper.create(userData);
	}

	@Override
	public UserData find(Long id) throws ServiceException {
		return mapper.find(id);
	}

	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
	@Override
	public void update(UserData data) throws ServiceException {
		mapper.update(data);
	}
}