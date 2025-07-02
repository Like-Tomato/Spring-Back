package com.like_lion.tomato.global.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public Info apiInfo() {
        return new Info()
                .version("1.0.0")
                .title("멋쟁이 토마토처럼")
                .description("멋사 웹페이지 제작 프로젝트 API 문서입니다.");
    }

    private SecurityScheme jwtSecurityScheme() {
        return new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER);
    }
}
