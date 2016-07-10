package producer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 基于验证的dubbo服务
 */
public class ValidatorProducer {
	public static void main(String[] args) throws IOException {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "dubbo-provider.xml" });
		context.start();
		System.out.println("启动成功之服务端验证参数");
		System.in.read();
	}
}
