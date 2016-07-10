package consumer;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import model.User;
import service.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

public class ValidatorUserConsumer {
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
		ReferenceConfig<UserService> userSerivce = new ReferenceConfig<UserService>(); // 此实例很重，封装了与注册中心的连接以及与提供者的连接，请自行缓存，否则可能造成内存和连接泄漏
		userSerivce.setValidation("true"); //客户端也打开验证
		userSerivce.setApplication(application);
		userSerivce.setRegistry(registry); // 多个注册中心可以用setRegistries()
		userSerivce.setInterface(UserService.class);
		userSerivce.setVersion("1.0.0");
		try {
			User u = new User();
			u.setId(1);
			u.setName("linhao");
			u.setPassword("xxx111");
			// 设置的隐式参数
			RpcContext.getContext().setAttachment("COOKIE", "5201314");
			userSerivce.get().create(u);
		} catch(ConstraintViolationException e){
            System.err.println("ConstraintViolationException occur");
            Set<ConstraintViolation<?>> violations = e
                    .getConstraintViolations(); // 可以拿到一个验证错误详细信息的集合
            System.out.println(violations);
            System.err.println(violations.iterator().next().getMessage());
        } catch (RpcException e) {
            e.printStackTrace();
		}
	}
}
