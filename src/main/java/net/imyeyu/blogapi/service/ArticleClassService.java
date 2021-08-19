package net.imyeyu.blogapi.service;

import net.imyeyu.blogapi.bean.ServiceException;
import net.imyeyu.blogapi.entity.ArticleClass;

import java.util.List;
import java.util.Map;

/**
 * 文章分类服务
 *
 * <p>夜雨 创建于 2021-07-04 19:29
 */
public interface ArticleClassService extends BaseService<ArticleClass> {

	Map<String, List<ArticleClass>> findBySide() throws ServiceException;
}