package com.pard.record_on_be.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Value("${recordon.server.domain}")
    private String domain;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url(domain))
                .info(new Info()
                        .title("Record ON")
                        .description("\"쉽게\",  \"부담감이 없도록\" 오직 경험에 대한 기록에 집중할 수 있도록 도와줍니다. ")
                        .version("0.9.0"));
    }
}
