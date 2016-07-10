package me.hao0.cglib.proxy;

import java.lang.reflect.Method;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class DynamicSubject implements MethodInterceptor {

	private Object realSubject;
	
    public Object getProxyInstance(Object target) {  
    	this.realSubject = target;
        Enhancer enhancer = new Enhancer();  
        enhancer.setSuperclass(realSubject.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }  
	
	@Override
	public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		System.out.println("调用前被代理类前..");
		Object result = proxy.invokeSuper(target, args);
		System.out.println("调用前被代理类前..");
		return result;
	}
}