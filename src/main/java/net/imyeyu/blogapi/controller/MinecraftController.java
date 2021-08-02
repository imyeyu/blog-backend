package net.imyeyu.blogapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Minecraft 相关接口
 *
 * <p>夜雨 创建于 2021-05-20 00:24
 */
@RestController
@RequestMapping("/mc")
public class MinecraftController extends BaseController {

	@GetMapping("")
	public String say() {
		return "minecraft api is working";
	}
}