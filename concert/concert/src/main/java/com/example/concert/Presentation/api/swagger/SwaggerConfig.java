package com.example.concert.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//Swagger Config
@Configuration
public class SwaggerConfig {

    // config 설정
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    //api config  설정
    private Info apiInfo() {
        return new Info()
                .title("CodeArena Swagger")
                .description("CodeArena 유저 및 인증 , ps, 알림에 관한 REST API")
                .version("1.0.0");
    }
}
