package me.hao0.bcel.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Author: haolin
 * On: 12/28/14
 */
public interface Interceptor {

    /**
     * 拦截调用方法
     * @param targetObject 目标对象
     * @param targetMethod 目标方法
     * @param args 方法参数
     * @return 方法调用结果
     */
    Object invoke(Object targetObject, Method targetMethod, Object... args) throws InvocationTargetException, IllegalAccessException;
}
