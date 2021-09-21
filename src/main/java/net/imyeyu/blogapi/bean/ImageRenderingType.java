package net.imyeyu.blogapi.bean;

/**
 * 图像缩放时的渲染算法，对应前端 CSS image-rendering 属性
 *
 * <p>夜雨 创建于 2021-09-20 11:49 AM
 */
public enum ImageRenderingType {

	AUTO,     // 双线性
	SMOOTH,   // 模糊
	PIXELATED // 像素
}