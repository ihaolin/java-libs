package me.hao0.producer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class DeclareProducer {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"dubbo-provider.xml"});
        context.start();
        System.out.println("启动成功之声明式dubbo服务");
        System.in.read();
    }
}
