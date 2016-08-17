package me.hao0.cxf.server;

import org.apache.cxf.frontend.ServerFactoryBean;
import me.hao0.cxf.service.impl.UserServiceImpl;

/**
 * 基于cxf frontend的服务端:
 * 并不会理会服务实现中的注解
 * @author haolin
 *
 */
public class CxfFrontendServerAPI {
	public static void main(String[] args) {
		UserServiceImpl service = new UserServiceImpl();
		ServerFactoryBean server = new ServerFactoryBean();
		server.setAddress("http://localhost:9999/UserService");
		server.setServiceBean(service);
		server.create();
	}
}
