package com.custom.butterknife.core;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by: Ysw on 2020/2/13.
 */
public class InjectClickInvocationHandler implements InvocationHandler {
    private Object object;
    private Method method;

    public InjectClickInvocationHandler(Object object, Method method) {
        this.object = object;
        this.method = method;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.d("Ysw", "invoke: method = " + method.getName());
        Log.d("Ysw", "invoke: this.method = " + this.method.getName());
        return this.method.invoke(object, args);
    }
}
