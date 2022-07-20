package com.angeya.bs.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Desc:
 *
 * @Author: Angeya
 * DateTime: 2021-09-22 21:43
 */

@Configuration
public class BeansConfig {
    @Autowired
    private MailConfig mailConfig;

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setDefaultEncoding(MailConfig.DEFAULT_CHARSET.toString());
        javaMailSender.setProtocol(mailConfig.getProtocol());
        javaMailSender.setHost(mailConfig.getHost());
        javaMailSender.setPort(mailConfig.getPort());
        javaMailSender.setUsername(mailConfig.getUsername());
        javaMailSender.setPassword(mailConfig.getPassword());
        return javaMailSender;
    }

    @Bean
    public ThreadPoolTaskExecutor mailSendExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setThreadNamePrefix("e-mail-executor");
        return taskExecutor;
    }

}
