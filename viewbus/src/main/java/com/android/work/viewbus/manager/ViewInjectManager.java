package com.android.work.viewbus.manager;

import android.app.Activity;
import android.view.View;

import com.android.work.viewbus.annotation.ContentView;
import com.android.work.viewbus.annotation.EventAnnotation;
import com.android.work.viewbus.annotation.OnClick;
import com.android.work.viewbus.annotation.InjectView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 项目名称：ViewAnnotation
 * 类描述：注解管理器
 * 作者：Mr.Liu
 * 创建时间：2019/10/26/ 9:42
 */
public class ViewInjectManager {

    /**
     * 处理加载布局的注解
     * */
    public static void inject(Activity activity){

        injectContentView(activity);
        injectView(activity);
        injectEvents(activity);

    }

    /**
     * 处理view的点击事件
     * */
    private static void injectEvents(final Activity activity) {

        //获取当前类对象的字节码对象
        final Class<? extends Activity> aClass = activity.getClass();
        //获取本类的所有方法
        Method[] methods = aClass.getDeclaredMethods();
        //遍历所有的方法
        for (final Method method :
                methods) {

            //获取每个方法的所有注解
            Annotation[] annotations = method.getAnnotations();
            //遍历注解，找到事件的注解
            for (Annotation annotation :
                    annotations) {

                //获取EventAnnotation注解对象
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType!=null) {
                    EventAnnotation eventAnnotation = annotationType.getAnnotation(EventAnnotation.class);
                    if (eventAnnotation != null) {

                        //获取事件设置器的监听器名称
                        String eventSet = eventAnnotation.eventSet();
                        //获取事件的监听器对象
                        final Class<?> eventType = eventAnnotation.eventType();
                        //设置事件处理的方法名
                        final String eventMethod = eventAnnotation.eventMethod();

                        //获取onClick注解中的值
                        try {
                            Method value = annotationType.getDeclaredMethod("value");
                            int[] values = (int[]) value.invoke(annotation);

                            /**
                             * 动态代理的实现 TODO 至于为啥，暂时还没有理解透彻
                             *
                             * ClassLoader loader  动态代理类接口的类加载器
                             * Class<?>[] interfaces,  动态代理类的实现接口
                             * InvocationHandler h
                             * */
                            Object o = Proxy.newProxyInstance(eventType.getClassLoader(), new Class[]{eventType}, new InvocationHandler() {
                                @Override
                                public Object invoke(Object o, Method method1, Object[] objects) throws Throwable {

                                    //当调用setOnClickListener()的onClick时，调用这里

                                    if (eventMethod.equals(method1.getName())) {
                                        if (objects.length == 0) {
                                            return method.invoke(activity);
                                        }
                                        return method.invoke(activity, objects);
                                    }

                                    return null;
                                }
                            });

                            //遍历事件id
                            for (int resId :
                                    values) {

                            /*//反射调用activity的findViewById方法初始化控件
                            Method findViewById = aClass.getMethod("findViewById", int.class);
                            findViewById.setAccessible(true);
                            //初始化相应的控件
                            View invoke = (View) findViewById.invoke(activity, resId);*/

                                View view = activity.findViewById(resId);

                                //通过拿到的view控件，进行反射调用setOnClickListener方法
                                Class<?> invokeClass = view.getClass();
                                Method m = invokeClass.getMethod(eventSet, eventType);

                                //当执行这句进入我们的代理模式中，进行aop切面技术
                                m.invoke(view, o);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }

            }

        }
    }

    /**
     * 处理view的初始化
     * */
    private static void injectView(Activity activity) {

        //获取当前类对象的字节码对象
        Class<? extends Activity> aClass = activity.getClass();
        //获取本类中的所有属性
        Field[] fields = aClass.getDeclaredFields();
        //遍历所有的属性
        for (Field field :
                fields) {
            //获取每个属性上的注解
            InjectView injectView = field.getAnnotation(InjectView.class);
            //存在注解，获取注解中的值
            if (injectView!=null){
                int value = injectView.value();
                //通过反射获取findViewById方法
                try {
                    Method findViewById = aClass.getMethod("findViewById", int.class);
                    Object invoke = findViewById.invoke(activity, value);

                    //将findViewById找到的值，赋值给变量
                    field.setAccessible(true);
                    field.set(activity,invoke);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }


    /**
     * 处理布局的加载
     * */
    private static void injectContentView(Activity activity) {

        //获取当前类的字节码对象
        Class<? extends Activity> aClass = activity.getClass();
        //获取当前的类注解
        ContentView contentView = aClass.getAnnotation(ContentView.class);
        //当存在注解时,获取注解值
        if (contentView!=null){
            int value = contentView.value();

            //通过反射技术调取setContentView方法
            try {
                Method setContentView = aClass.getMethod("setContentView", int.class);
                setContentView.invoke(activity,value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
