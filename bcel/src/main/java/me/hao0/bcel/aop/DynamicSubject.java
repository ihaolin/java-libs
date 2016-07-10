package me.hao0.bcel.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Author: haolin
 * On: 12/28/14
 */
public class DynamicSubject implements Interceptor {

    /**
     * 生成代理类
     * @param proxyed 被代理类
     * @return
     */
    public Object newProxyInstance(Object proxyed){
        return ProxyClass.getProxyClass(proxyed, this);
    }

    /**
     * 拦截调用方法
     *
     * @param targetObject 目标对象
     * @param targetMethod 目标方法
     * @param args         方法参数
     * @return 方法调用结果
     */
    @Override
    public Object invoke(Object targetObject, Method targetMethod, Object... args) throws InvocationTargetException, IllegalAccessException {
        System.out.println("执行被代理类方法前");
        Object result = targetMethod.invoke(targetObject, args);
        System.out.println("执行被代理类方法后");
        return result;
    }
}
