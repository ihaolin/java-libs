package me.hao0.consumer;

import me.hao0.context.DemoContext;
import me.hao0.dubbo.model.User;
import me.hao0.dubbo.service.DemoService;
import me.hao0.dubbo.service.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DeclareConsumer {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] {"dubbo-consumer.xml"});
		context.start();


		DemoService demoService = (DemoService) context.getBean("demoService"); // 获取远程服务代理
		String hello = demoService.sayHello("world"); // 执行远程方法

        // return result
		System.out.println(hello);


		// SET some thread local context
		DemoContext.set("You're shine.");

		UserService userService = (UserService)context.getBean("userService");

		User user = new User();
		user.setId(1);
		user.setName("haolin");
		user.setPassword("123456");
		user.setUsername("haolin");

		// RpcContext.getContext().set("COOKIE", "FUCK U");

		userService.create(user);
	}
}
