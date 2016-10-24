package me.hao0.filter;

import com.alibaba.dubbo.rpc.*;
import me.hao0.context.DemoContext;

/**
 * Author: haolin
 * Email:  haolin.h0@gmail.com
 */
public class DemoFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        System.out.println("demo context: " + DemoContext.get());

        System.out.println("interface: " + invoker.getInterface());

        System.out.println("method: " + invocation.getMethodName());

        invocation.getAttachments().put("COOKIE", "FUCK U");

        System.out.println("args: ");
        for (Object arg : invocation.getArguments()){
            System.out.println(arg + " ");
        }
        System.out.println();

        System.out.println("attachments: " + invocation.getAttachments());

        Result r = invoker.invoke(invocation);

        System.out.println(r);

        return r;
    }
}
