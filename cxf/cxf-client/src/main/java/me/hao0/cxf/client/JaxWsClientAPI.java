package me.hao0.cxf.client;

import me.hao0.cxf.model.User;
import me.hao0.cxf.service.UserService;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 通过jax ws api的方式调用服务
 */
public class JaxWsClientAPI {
	/**
	 * 服务所在命名空间, 反包名, 即wsdl:definitions中的targetNamespace
	 */
	private static final String targetNamespace = "http://service.cxf.haolin.org/";
	
	/**
	 * 服务名: wsdl:service
	 */
	private static final QName SERVICE_NAME = new QName(targetNamespace, "UserService");
	
	/**
	 * 端口名: wsdl:port
	 */
	private static final QName PORT_NAME = new QName(targetNamespace, "UserServicePort");
	
	/**
	 * wsdl地址: soap:address
	 */
	private static final String WSDL_LOCATION = "http://localhost:8888/UserService?wsdl";

	public static void main(String[] args) throws MalformedURLException {
		URL wsdlURL = new URL(WSDL_LOCATION);
		Service service = Service.create(wsdlURL, SERVICE_NAME);
		UserService userService = service.getPort(PORT_NAME, UserService.class);
		
		User u = new User();
		u.setId(1L);
		u.setUsername("haolin");
		u.setPassword("haolin");
		u.setMobile("18683443875");
		userService.create(u);
		System.out.println(userService.list());
	}
}
