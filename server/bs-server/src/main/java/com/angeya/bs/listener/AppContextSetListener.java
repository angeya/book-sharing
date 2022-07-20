package com.angeya.bs.listener;

import com.angeya.bs.util.BeanUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Author: Angeya
 * @date: 2021/8/5 11:09
 */
@Component
public class AppContextSetListener implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanUtil.setApplicationContext(applicationContext);
    }
}
