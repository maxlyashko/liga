package ua.lyashko.liga.configuration;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    private static final String TITLE = "Liga Zakon test";
    private static final String DESCRIPTION = "Service for extract any data";
    private static final String VERSION = "0.0.1-SNAPSHOT";

    @Bean
    public OpenAPI openApi() {
        final var info = new Info().title(TITLE).description(DESCRIPTION).version(VERSION);
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(AUTHORIZATION))
                .components(new Components()
                        .addSecuritySchemes(AUTHORIZATION,
                                new SecurityScheme().type(SecurityScheme.Type.HTTP)))
                .info(info);
    }
}
