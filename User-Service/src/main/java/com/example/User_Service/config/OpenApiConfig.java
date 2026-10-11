package com.example.User_Service.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "keycloak",
        type = SecuritySchemeType.OAUTH2,
        flows =@OAuthFlows(
                password = @OAuthFlow(
                        tokenUrl = "http://localhost:8080/realms/instagram/protocol/openid-connect/token"
                )
        )
)
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("User Service").version("v1"))
                .addSecurityItem(new SecurityRequirement().addList("keycloak"));
    }
}
