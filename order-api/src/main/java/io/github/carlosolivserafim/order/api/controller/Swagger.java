package io.github.carlosolivserafim.order.api.controller;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger {

    @Bean
    public OpenAPI config(){
        return new OpenAPI()
                .info(new Info()
                        .title("Documentação da API de Gerenciamento de Orders")
                        .version("1.0.0"));
    }
}