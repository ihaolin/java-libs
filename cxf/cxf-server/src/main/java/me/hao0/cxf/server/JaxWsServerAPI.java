package me.hao0.cxf.server;

import org.haol.learn.cxf.service.impl.UserServiceImpl;

import javax.xml.ws.Endpoint;

/**
 * 通过jax ws api的方式发布服务
 */
public class JaxWsServerAPI {
	public static void main(String[] args) throws InterruptedException {
		UserServiceImpl userServiceImpl = new UserServiceImpl();
		String address = "http://localhost:9999/UserService";
		Endpoint.publish(address, userServiceImpl);
		Thread.sleep(10000000);
	}
}
