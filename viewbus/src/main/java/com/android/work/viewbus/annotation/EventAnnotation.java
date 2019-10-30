package com.android.work.viewbus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 项目名称：ViewAnnotation
 * 类描述：用与事件注解上的注解
 * 作者：Mr.Liu
 * 创建时间：2019/10/26/ 23:22
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventAnnotation {

    /**
     * 获取事件的setXXXXX()方法
     * */
    String eventSet();

    /**
     * 获取事件的监听对象
     * */
    Class<?> eventType();

    /**
     * 获取事件执行方法
     * */
    String eventMethod();
}
