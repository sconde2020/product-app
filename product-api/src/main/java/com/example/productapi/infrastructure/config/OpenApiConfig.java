package com.example.productapi.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product API")
                        .description("Spring Boot DDD Product API with OpenAPI documentation")
                        .version("1.0.0"));
    }
}
