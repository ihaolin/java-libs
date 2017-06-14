package me.hao0.thrift.server;

import me.hao0.thrift.server.service.HelloServiceImpl;
import me.hao0.thrift.service.HelloService;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;

/**
 * Author: linhao
 * Email: linhao09@meituan.com
 */
public class Bootstrap {

    public static void main(String[] args){
        try {

            System.out.println("TSimpleServer start ....");

            TProcessor tprocessor = new HelloService.Processor<HelloService.Iface>(
                    new HelloServiceImpl());

            TServerSocket serverTransport = new TServerSocket(9001);
            TServer.Args tArgs = new TServer.Args(serverTransport);
            tArgs.processor(tprocessor);
            tArgs.protocolFactory(new TBinaryProtocol.Factory());
            TServer server = new TSimpleServer(tArgs);

            server.serve();

        } catch (Exception e) {
            System.out.println("Server start error!!!");
            e.printStackTrace();
        }
    }
}
