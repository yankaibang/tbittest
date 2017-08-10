package com.example.tbittestlib.bike.util.proxy;

import java.lang.reflect.Proxy;

/**
 * Created by Salmon on 2017/3/19 0019.
 */

public class ProxyUtil {

    public static <T> T getProxy(Object object) {
        return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(),
                object.getClass().getInterfaces(), new ProxyInvocationHandler(object));
    }
}
