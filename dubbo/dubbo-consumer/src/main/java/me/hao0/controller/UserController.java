package me.hao0.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import me.hao0.dubbo.service.UserService;
import org.springframework.stereotype.Component;

/**
 * 控制器
 */
@Component
public class UserController {
	
	@Reference
	private UserService userService;
	
	public void login(String user, String pass){
		userService.login(user, pass);
	}
}
