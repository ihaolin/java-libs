package me.hao0.cxf.client;

import me.hao0.cxf.model.User;
import me.hao0.cxf.service.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * jaxws client demo
 */
public class JaxWsClient {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = 
				new ClassPathXmlApplicationContext("spring-jaxws-client.xml");
		context.start();
		UserService userService = (UserService)context.getBean("userService");
		User u = new User();
		u.setId(1L);
		u.setUsername("haolin");
		u.setPassword("haolin");
		u.setMobile("18683443875");
		userService.create(u);
		
		System.out.println(userService.list());
		context.close();
	}
}
