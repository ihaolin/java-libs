package controller;

import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;
import service.UserService;

/**
 * 控制器
 */
@Component
public class UserController {
	
	@Reference(version="1.0.0")
	private UserService userService;
	
	public void login(String user, String pass){
		userService.login(user, pass);
	}
}
