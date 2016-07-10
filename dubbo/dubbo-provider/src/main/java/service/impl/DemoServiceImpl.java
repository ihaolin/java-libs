package service.impl;

import service.DemoService;

/**
 * 服务实现累
 * @author haolin
 */
public class DemoServiceImpl implements DemoService {

	public String sayHello(String name) {
		return "Hello, " + name;
	}
}