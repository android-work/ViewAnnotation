package com.android.work.viewbus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 项目名称：ViewAnnotation
 * 类描述：布局注解器
 * 作者：Mr.Liu
 * 创建时间：2019/10/26/ 9:33
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentView {

    /**
     * 获取注解里的值
     */
    int value();
}
