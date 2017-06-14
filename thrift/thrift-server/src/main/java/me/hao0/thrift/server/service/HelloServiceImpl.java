package me.hao0.thrift.server.service;

import me.hao0.thrift.service.HelloService;
import org.apache.thrift.TException;

/**
 * Author: linhao
 * Email: linhao09@meituan.com
 */
public class HelloServiceImpl implements HelloService.Iface {

    public String hello(String name) throws TException {
        return "Hello, " + name;
    }
}
