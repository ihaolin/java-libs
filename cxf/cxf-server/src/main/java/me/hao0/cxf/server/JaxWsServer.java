package me.hao0.cxf.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Jaxws org.haolin.cxf.server
 */
public class JaxWsServer {
	public static void main(String[] args) throws InterruptedException {
		ClassPathXmlApplicationContext context = 
				new ClassPathXmlApplicationContext("spring-jaxws-server.xml");
		context.start();
		
		Thread.sleep(1000000);
		
		context.close();
	}
}
