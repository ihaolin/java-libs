package service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.RpcContext;
import model.User;
import service.UserService;

/**
 * dubbo服务实现
 */
@Service(version="1.0.0",retries=2, validation="true")
public class UserServiceImpl implements UserService {

	public void login(String user, String pass) {
		System.out.println("user["+user+"] with password["+pass+"]");
	}

	@Override
	public void create(User user) {
		System.out.println("create user successfully.");
		// 获取消费端设置的隐式参数
		System.out.println(RpcContext.getContext().getAttachment("COOKIE"));
	}
}
