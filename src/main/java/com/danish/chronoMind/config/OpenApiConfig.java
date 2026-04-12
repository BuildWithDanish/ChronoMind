package com.danish.chronoMind.config;

import io.swagger.v3.oas.models.Components;
import org.springframework.beans.factory.annotation.Value;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${app.env}")
    private String env;
    @Bean
    public OpenAPI journalAppOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ChronoMind API")
                        .version("v1.0")
                        .description("Personal journaling platform with categorized entries and automated weekly email reports for insights like productivity tracking.\n\n" +
                                "Journal entries are categorized into two types:\n" +
                                "\n" +
                                "- PRODUCTIVITY: Entries used for tracking user productivity. These are included in automated weekly email reports.\n" +
                                "\n" +
                                "- CASUAL: Entries used for general daily journaling. These are excluded from weekly reports."+
                                "\n\n By Mohammad Danish"))
                .servers(Arrays.asList(
                        new Server()
                                .url(baseUrl)
                                .description(env)))
                // Bearer JWT scheme define kar rahe hain — sab protected endpoints pe lock icon aayega
                .components(new Components()
                        .addSecuritySchemes("Bearer Auth", new SecurityScheme()
                                .name("Bearer Auth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}