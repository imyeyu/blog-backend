package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.UserData;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户数据服务
 *
 * <p>夜雨 创建于 2021-07-27 17:05
 */
public interface UserDataService extends BaseService<UserData> {

	/**
	 * 根据用户 ID 查找用户资料
	 *
	 * @param uid 用户 ID
	 * @return 用户资料
	 * @throws ServiceException 服务异常
	 */
	UserData findByUID(Long uid) throws ServiceException;

	/**
	 * 更新头像
	 *
	 * @param id   用户 ID
	 * @param file 上传文件对象
	 * @return 回调该资源路径
	 * @throws ServiceException 服务异常
	 */
	String updateAvatar(Long id, MultipartFile file) throws ServiceException;

	/**
	 * 更新背景图
	 *
	 * @param id   用户 ID
	 * @param file 上传文件对象
	 * @return 回调该资源路径
	 * @throws ServiceException 服务异常
	 */
	String updateWrapper(Long id, MultipartFile file) throws ServiceException;
}