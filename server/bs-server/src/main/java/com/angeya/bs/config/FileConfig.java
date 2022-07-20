package com.angeya.bs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Description:
 * @Author: Angeya
 * DateTime: ${YEAR}-${MONTH}-${DAY} ${TIME}
 */
@ConfigurationProperties(prefix = "app.file")
@Component
public class FileConfig {

    private String basePath;

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}
