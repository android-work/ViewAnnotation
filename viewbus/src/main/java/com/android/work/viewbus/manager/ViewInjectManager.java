package com.android.work.viewbus.manager;

import android.app.Activity;

import com.android.work.viewbus.annotation.ContentView;
import com.android.work.viewbus.annotation.EventAnnotation;
import com.android.work.viewbus.annotation.onClick;
import com.android.work.viewbus.annotation.InjectView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
    private static void injectEvents(Activity activity) {

        //获取当前类对象的字节码对象
        Class<? extends Activity> aClass = activity.getClass();
        //获取本类的所有方法
        Method[] methods = aClass.getDeclaredMethods();
        //遍历所有的方法
        for (Method method :
                methods) {

            //获取每个方法的所有注解
            Annotation[] annotations = method.getAnnotations();
            //遍历注解，找到事件的注解
            for (Annotation annotation :
                    annotations) {

                //获取EventAnnotation注解对象
                Class<? extends Annotation> annotationClass = annotation.annotationType();
                EventAnnotation eventAnnotation = annotationClass.getAnnotation(EventAnnotation.class);

                if (eventAnnotation!=null) {
                    //获取事件设置器的监听器名称
                    String eventSet = eventAnnotation.eventSet();
                    //获取事件的监听器对象
                    Class<?> eventType = eventAnnotation.eventType();
                    //设置事件处理的方法名
                    String eventMethod = eventAnnotation.eventMethod();

                    //获取injectEvents的注解对象
                    onClick onClick = annotationClass.getAnnotation(onClick.class);
                    //获取事件注解中的所有值
                    int[] value = onClick.value();
                    


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
