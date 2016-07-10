package service;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 服务接口
 * @author haolin
 */
public interface DemoService {
	String sayHello(
            @NotBlank(message = "name.blank")
            String name
    );
}
