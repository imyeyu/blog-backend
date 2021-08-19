package net.imyeyu.blogapi.service.implement;

import net.imyeyu.blogapi.bean.ReturnCode;
import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.ArticleClass;
import net.imyeyu.blogapi.mapper.ArticleClassMapper;
import net.imyeyu.blogapi.service.ArticleClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文章分类服务实现
 *
 * <p>夜雨 创建于 2021-07-04 19:29
 */
@Service
public class ArticleClassServiceImplement implements ArticleClassService {

	@Autowired
	private ArticleClassMapper mapper;

	@Override
	public ArticleClass find(Long id) throws ServiceException {
		ArticleClass ac = mapper.find(id);
		if (ac != null) {
			return ac;
		} else {
			throw new ServiceException(ReturnCode.RESULT_NULL, "找不到该分类");
		}
	}

	@Override
	public Map<String, List<ArticleClass>> findBySide() {
		Map<String, List<ArticleClass>> map = new HashMap<>();
		map.put("main", mapper.findMain());
		map.put("other", mapper.findOther());
		return map;
	}
}