package com.example.apisocialmeli.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SocialMeli API")
                        .description("Social network API for Mercado Livre buyers and sellers")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Bootcamp MeLi")
                                .email("contato@meli.com")));
    }
}