package com.example.tbittestlib.bike.util.proxy;

import java.lang.reflect.Method;

/**
 * Created by Salmon on 2017/3/19 0019.
 */

public class ProxyBulk {

    private Object object;
    private Method method;
    private Object[] args;

    public ProxyBulk(Object object, Method method, Object[] args) {
        this.object = object;
        this.method = method;
        this.args = args;
    }

    public Object safeInvoke() {
        Object result = null;
        try {
            result = method.invoke(object, args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return result;
    }
}
