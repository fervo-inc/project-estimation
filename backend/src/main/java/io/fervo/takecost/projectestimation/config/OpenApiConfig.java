package io.fervo.takecost.projectestimation.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        var securitySchemeName = "BearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("TakeCost Project Estimation API")
                        .version("0.0.1") // TODO: Display the release version and the commit hash
                        .description("API documentation for the Project Estimation Module of TakeCost"))
                .servers(new ArrayList<Server>() {{
                    add(new Server().url("http://localhost:8080").description("Localhost Server"));
                    // TODO: For deployment, load the hostname from the properties
                    add(new Server().url("https://api.prod-takecost.com").description("Production Server"));
                }})
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .schemaRequirement(securitySchemeName, new SecurityScheme()
                        .name(securitySchemeName)
                        .description("JWT Authentication")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .in(SecurityScheme.In.HEADER)
                        .bearerFormat("JWT"));
    }
}
