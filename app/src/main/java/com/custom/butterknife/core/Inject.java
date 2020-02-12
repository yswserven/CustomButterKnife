package com.custom.butterknife.core;

import android.view.View;

import com.custom.butterknife.annotation.ContentView;
import com.custom.butterknife.annotation.EventBase;
import com.custom.butterknife.annotation.ViewInject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by: Ysw on 2020/2/12.
 */
public class Inject {
    public static void inject(Object object) {
        injectContentView(object);
        injectView(object);
        injectClick(object);
    }

    /**
     * 注入 Activity 布局 Layout
     *
     * @author Ysw created at 2020/2/13 0:07
     */
    private static void injectContentView(Object object) {
        Class<?> clazz = object.getClass();
        ContentView annotation = clazz.getAnnotation(ContentView.class);
        if (annotation != null) {
            int value = annotation.value();
            try {
                Method method = clazz.getMethod("setContentView", int.class);
                method.invoke(object, value);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 注入 控件
     *
     * @author Ysw created at 2020/2/13 0:11
     */
    private static void injectView(Object object) {
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            ViewInject annotation = field.getAnnotation(ViewInject.class);
            if (annotation != null) {
                int value = annotation.value();
                try {
                    Method method = clazz.getMethod("findViewById", int.class);
                    View view = (View) method.invoke(object, value);
                    field.setAccessible(true);
                    field.set(object, view);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 控件点击事件的注入
     *
     * @author Ysw created at 2020/2/13 2:35
     */
    private static void injectClick(Object object) {
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                if (eventBase == null) continue;
                String listenerSetter = eventBase.listenerSetter();
                Class<?> listenerType = eventBase.listenerType();
                // String callbackMethod = eventBase.callbackMethod();
                Method valueMethod;
                try {
                    valueMethod = annotationType.getDeclaredMethod("value");
                    int[] viewId = (int[]) valueMethod.invoke(annotation);
                    assert viewId != null;
                    for (int id : viewId) {
                        Method findViewById = clazz.getMethod("findViewById", int.class);
                        View view = (View) findViewById.invoke(object, id);
                        if (view == null) continue;
                        InjectClickInvocationHandler handler = new InjectClickInvocationHandler(object, method);
                        Proxy proxy = (Proxy) Proxy.newProxyInstance(listenerType.getClassLoader(),
                                new Class[]{listenerType}, handler);
                        Method viewClickMethod = view.getClass().getMethod(listenerSetter, listenerType);
                        viewClickMethod.invoke(view, proxy);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
