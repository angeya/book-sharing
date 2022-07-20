package com.angeya.bs.util;

import org.springframework.context.ApplicationContext;

/**
 * 获取 bean 工具类
 * @Author: Angeya
 * @date: 2021/8/5 11:06
 */

public class BeanUtil {
    private static ApplicationContext appCtx;

    public static void setApplicationContext(ApplicationContext ctx) {
        appCtx = ctx;
    }

    public static<T> T getBean(String beanName, Class<T> type) {
        return appCtx.getBean(beanName, type);
    }

    public static<T> T getBean(Class<T> type) {
        return appCtx.getBean(type);
    }

}
