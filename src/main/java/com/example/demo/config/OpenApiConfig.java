package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Health In Your Hands APIs")
                        .description("API documentation for lab interpretations/ biomarkers APIs")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Team 7 - Wait for Green"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org"))
                );
    }
}