package com.android.work.viewbus.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 项目名称：ViewAnnotation
 * 类描述：控件的点击事件
 * 作者：Mr.Liu
 * 创建时间：2019/10/26/ 15:57
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventAnnotation(eventSet = "setOnClickListener", eventType = View.OnClickListener.class, eventMethod = "onClick")
public @interface OnClick {

    /**
     * 获取注解中注解的值
     * */
    int[] value();
}
