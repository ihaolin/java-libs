package me.hao0.bcel.demo;

/**
 * Author: haolin
 * On: 12/28/14
 */
public class MyClassLoader extends ClassLoader {

    public Class<?> defineClass(String name, byte[] bytes){
        return super.defineClass(name, bytes, 0, bytes.length);
    }
}
