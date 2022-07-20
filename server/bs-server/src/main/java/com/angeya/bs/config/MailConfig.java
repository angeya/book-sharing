package com.angeya.bs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Desc: 邮件服务相关配置参数
 * @Author: Angeya
 * DateTime: 2021-09-22 21:35
 */

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class MailConfig {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private String protocol = "smtp";
    private String host;
    private Integer port;
    private String username;
    private String password;

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
