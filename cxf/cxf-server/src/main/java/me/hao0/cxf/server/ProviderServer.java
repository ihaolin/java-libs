package me.hao0.cxf.server;

import me.hao0.cxf.providers.UserServiceDOMProvider;

import javax.xml.ws.Endpoint;

/**
 * 基于服务提供者发布接口
 * @author haolin
 *
 */
public class ProviderServer {
	public static void main(String[] args) {
		Object implementor = 
				new UserServiceDOMProvider();
		String address = "http://localhost:9999/UserServiceDOMProvider";
		Endpoint.publish(address, implementor);
		System.out.println(10000000);
	}
}
