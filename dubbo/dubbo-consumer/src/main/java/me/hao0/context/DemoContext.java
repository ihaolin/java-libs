package me.hao0.context;

/**
 * Author: haolin
 * Email:  haolin.h0@gmail.com
 */
public class DemoContext {

    private DemoContext(){}

    private static ThreadLocal<String> info = new ThreadLocal<>();

    public static void set(String info){
        DemoContext.info.set(info);
    }

    public static String get(){
        return info.get() == null ? null : info.get();
    }

    public static boolean isLogin(){
        return info.get() != null;
    }

    public static void clear(){
        info.remove();
    }

}
