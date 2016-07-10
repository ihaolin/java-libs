package me.hao0.cglib.proxy;

public class ProxyClient {

    public static void main(String[] args){

        DynamicSubject subject = new DynamicSubject();

        Subject sub = (Subject)subject.getProxyInstance(new RealSubject());

        sub.request();
    }
}
