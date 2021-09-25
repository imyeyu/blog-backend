package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ResourceFile;
import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.UserData;
import net.imyeyu.blogapi.mapper.UserDataMapper;
import net.imyeyu.blogapi.service.FileService;
import net.imyeyu.blogapi.service.UserDataService;
import net.imyeyu.blogapi.service.UserService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
		if (ObjectUtils.isNotEmpty(data.getSex())) {
			if (data.getSex() != 0 && data.getSex() != 1) {
				throw new ServiceException(ReturnCode.PARAMS_BAD, "请选择正确的性别");
			}
		}
		if (ObjectUtils.isNotEmpty(data.getBirth())) {
			if (data.getBirth() < 0) {
				throw new ServiceException(ReturnCode.PARAMS_BAD, "出生日期不可小于 1970-01-01");
			}
			if (System.currentTimeMillis() < data.getBirth()) {
				throw new ServiceException(ReturnCode.PARAMS_BAD, "出生日期不可超过现在");
			}
		}
		if (StringUtils.isNotEmpty(data.getQq()) && !data.getQq().matches("[1-9]\\d{4,14}")) {
			throw new ServiceException(ReturnCode.PARAMS_BAD, "请输入正确的 QQ 号码");
		}
		if (StringUtils.isNotEmpty(data.getSign()) && 240 < data.getSign().length()) {
			throw new ServiceException(ReturnCode.PARAMS_BAD, "签名字数不可超过 240 个");
		}
		data.setUpdatedAt(System.currentTimeMillis());
		mapper.updateData(data);
	}

	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
	@Override
	public String updateAvatar(Long id, MultipartFile file) throws ServiceException {
		try {
			BufferedImage img = ImageIO.read(file.getInputStream());
			// 前端正常裁切尺寸为 256
			if (img.getWidth() != 256 || img.getHeight() != 256) {
				throw new ServiceException(ReturnCode.REQUEST_BAD, "无效的请求");
			}
			// 处理文件
			ResourceFile res = new ResourceFile();
			res.setName(id + ".png");
			res.setPath(pathAvatar);
			res.setInputStream(file.getInputStream());
			fileService.upload(res);
			// 更新数据库
			UserData data = findByUID(id);
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
			// 字节数据
			byte[] bytes = file.getInputStream().readAllBytes();
			if (1048576 < bytes.length) {
				throw new ServiceException(ReturnCode.PARAMS_BAD, "限制上传文件大小 1 MB");
			}
			// 原图
			ResourceFile res = new ResourceFile();
			res.setName(id + ".png");
			res.setPath(pathWrapper);
			res.setInputStream(new ByteArrayInputStream(bytes));
			fileService.upload(res);
			// 资料卡缩略图（375 x 100）
			UserData userData = mapper.findByUID(id);
			// 根据选择算法缩放
			BufferedImage imgSrc = ImageIO.read(new ByteArrayInputStream(bytes));
			final double scale = 375D / imgSrc.getWidth();
			final int width = 375;
			final int height = (int) (width * scale);
			BufferedImage imgResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = imgResult.createGraphics();
			switch (userData.getWrapperRenderingType()) {
				case AUTO, SMOOTH -> {
					g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
					g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
					g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
					g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
					g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				}
				case PIXELATED -> g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			}
			g.drawImage(imgSrc, 0, 0, width, height, null);
			g.dispose();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(imgResult, "png", baos);

			ResourceFile resPopup = new ResourceFile();
			resPopup.setName(id + ".png");
			resPopup.setPath(pathWrapper + "_popup");
			resPopup.setInputStream(new ByteArrayInputStream(baos.toByteArray()));
			fileService.upload(resPopup);
			// 更新数据库
			UserData data = findByUID(id);
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

	@Transactional(rollbackFor = {ServiceException.class, Throwable.class})
	@Override
	public void update(UserData data) throws ServiceException {
		mapper.update(data);
	}
}