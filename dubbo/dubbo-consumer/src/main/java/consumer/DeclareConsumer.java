package consumer;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.DemoService;

public class DeclareConsumer {
	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "dubbo-consumer.xml" });
		context.start();

		DemoService demoService = (DemoService) context.getBean("demoService"); // 获取远程服务代理
		String hello = demoService.sayHello("world"); // 执行远程方法

        // return result
		System.out.println(hello);
		
		//UserController userController = (UserController)context.getBean("userController");
		
		//userController.login("haolin", "111111");
	}
}
