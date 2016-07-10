package test;

import me.hao0.cxf.service.UserService;
import me.hao0.cxf.service.UserService_Service;
import org.junit.Test;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 用户服务测试
 */
public class UserServiceTest {
	
	@Test
	public void testList() throws MalformedURLException{
		URL wsdl = new URL("http://localhost:9999/UserService?wsdl");
		QName service = new QName("http://service.cxf.learn.haol.org/", "UserService");
		
		UserService_Service uss =
				new UserService_Service(wsdl, service);
		UserService userService = uss.getUserServicePort();
		System.out.println(userService.list().size());
	
		// 默认用从wsdl生成的配置
		UserService_Service ussDefault = new UserService_Service();
		userService = ussDefault.getUserServicePort();
		System.out.println(userService.list().size());
	}
}
