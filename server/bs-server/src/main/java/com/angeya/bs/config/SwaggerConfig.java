package com.angeya.bs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: Angeya
 * @date: 2021/9/3 15:29
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket showApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("show")
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .pathMapping("/")
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo("book-sharing Restful API",
                "book-sharing REST API",
                "1.0",
                "NO terms of service",
                "book-sharing",
                "Copyright angeya 2019 - ",
                "http://www.angeya.com"
        );
    }
}
