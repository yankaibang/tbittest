package com.example.yankaibang.bike.util.proxy;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by Salmon on 2017/3/19 0019.
 */

public class ProxyInvocationHandler implements InvocationHandler, Handler.Callback {

    private Object subject;
    private Handler handler;

    public ProxyInvocationHandler(Object subject) {
        this.subject = subject;
        handler = new Handler(Looper.getMainLooper(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ProxyBulk proxyBulk = new ProxyBulk(subject, method, args);
        handler.obtainMessage(0, proxyBulk);
        return null;
    }

    @Override
    public boolean handleMessage(Message msg) {
        ((ProxyBulk) msg.obj).safeInvoke();
        return true;
    }
}
