package com.sideforge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for sideForge API.
 * This configuration sets up Swagger UI with metadata describing the sideForge platform,
 * which supports 3D asset customization, scene management, and user roles.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("sideForge API")
                        .version("1.0.0")
                        .description(
                                "Side3D API enables the management of customizable 3D scenes and assets for end users and administrators.\n\n" +
                                        "Main features:\n" +
                                        "- **Users & Roles:** Handles admin and customer roles, user authentication and verification.\n" +
                                        "- **Assets:** Manage base 3D assets (e.g., t-shirt, mug) with customizable parts and configuration.\n" +
                                        "- **Designs:** Customize assets through textures, materials, colors, logos, and text overlays.\n" +
                                        "- **Scenes:** Configure 3D scenes with camera, lighting, and associated customized designs.\n" +
                                        "- **Ownership & Access:** Link scenes to their owning users, and enforce access controls on customization.\n\n" +
                                        "The API is structured around resource-oriented endpoints supporting the complete lifecycle of 3D asset customization and scene management."
                        )
                        .contact(new Contact()
                                .name("sideForge by krub-dev")
                                .url("https://github.com/krub-dev/sideForge")));
    }
}