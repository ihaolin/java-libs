package me.hao0.thrift.client;

import me.hao0.thrift.service.HelloService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * Author: linhao
 * Email: linhao09@meituan.com
 */
public class Bootstrap {

    public static void main(String[] args) throws TException {

        TTransport transport = new TSocket("127.0.0.1", 9001, 30000);

        transport.open();

        TProtocol protocol = new TBinaryProtocol(transport);

        HelloService.Client client = new HelloService.Client(protocol);

        System.out.println(client.hello("haolin"));

        transport.close();
    }
}
