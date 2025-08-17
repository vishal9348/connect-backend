package com.vishal.user_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Connect to world-wide App")
                        .description("We can connect to people all over the world")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Vishal")
                                .url("https://www.linkedin.com/in/vishal9348")
                                .email("vishalraj27599@gmail.com"))
                        .license(new License().name("Vishal license").url("vishal.com"))
                )
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("com.vishal")
                .pathsToMatch("/**")
                .pathsToExclude("/api/v1/user/login", "/api/v1/user/register")
                .packagesToScan("com.vishal.user_service.controller")
                .build();
    }

    @Bean
    public OpenApiCustomizer globalSecurityCustomiser() {
        // Define public paths (to exclude from security)
        List<String> publicPaths = Arrays.asList(
                "/api/v1/user/login",
                "/api/v1/user/register"
        );

        return openApi -> openApi.getPaths().forEach((path, pathItem) -> {
            if (publicPaths.contains(path)) return; // Skip public paths

            pathItem.readOperations().forEach(operation ->
                    operation.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            );
        });
    }
}
