package me.hao0.consumer;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.RpcException;
import me.hao0.dubbo.service.DemoService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

public class ValidatorHelloConsumer {
	public static void main(String[] args) {
		// 当前应用配置
		ApplicationConfig application = new ApplicationConfig();
		application.setName("hello-validate-consumer");

		// 连接注册中心配置
		RegistryConfig registry = new RegistryConfig();
		registry.setAddress("zookeeper://localhost:2181");

		// 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接
		// 引用远程服务
		ReferenceConfig<DemoService> demoSerivce = new ReferenceConfig<DemoService>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
		demoSerivce.setValidation("true"); //客户端也打开验证
		demoSerivce.setApplication(application);
		demoSerivce.setRegistry(registry); // 多个注册中心可以用setRegistries()
		demoSerivce.setInterface(DemoService.class);
		try {
            DemoService service = demoSerivce.get();
            System.out.println(service.sayHello(""));
		} catch(ConstraintViolationException e){
            System.err.println("ConstraintViolationException occur.");
            Set<ConstraintViolation<?>> violations = e
                    .getConstraintViolations(); // 可以拿到一个验证错误详细信息的集合
            System.out.println(violations);
            System.err.println(violations.iterator().next().getMessage());
        } catch (Exception e) {
            if (e instanceof  RpcException){
                RpcException re = (RpcException)e;
                if (re.getCause() instanceof ConstraintViolationException){
                    ConstraintViolationException cve = (ConstraintViolationException)re.getCause();
                    Set<ConstraintViolation<?>> violations = cve
                            .getConstraintViolations(); // 可以拿到一个验证错误详细信息的集合
                    System.out.println(violations);
                    System.err.println(violations.iterator().next().getMessage());
                }
            }
            //e.printStackTrace();
		}
	}
}
