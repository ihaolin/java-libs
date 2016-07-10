package consumer;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import service.DemoService;

/**
 * 编程式消费服务 消费者直连提供者者，不经过注册中心
 * reference.setUrl("dubbo://localhost:20888/service.DemoService");
 */
public class P2PConsumer {
	public static void main(String[] args) {
		// 当前应用配置
		ApplicationConfig application = new ApplicationConfig();
		application.setName("yyy");

		// 连接注册中心配置
		RegistryConfig registry = new RegistryConfig();
		registry.setAddress("zookeeper://localhost:2181");
		// registry.setUsername("aaa");
		// registry.setPassword("bbb");

		// 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接

		// 引用远程服务
		ReferenceConfig<DemoService> reference = new ReferenceConfig<DemoService>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
		reference.setApplication(application);
		//reference.setRegistry(registry); // 多个注册中心可以用setRegistries()
		//不经过注册中心，直接连接提供者
		reference.setUrl("dubbo://localhost:20888/service.DemoService");
		reference.setInterface(DemoService.class);
		reference.setVersion("1.0.0");

		// 和本地bean一样使用xxxService
		DemoService demoService = reference.get(); // 注意：此代理对象内部封装了所有通讯细节，对象较重，请缓存复用
		System.out.println(demoService.sayHello("linhao"));
	}
}
