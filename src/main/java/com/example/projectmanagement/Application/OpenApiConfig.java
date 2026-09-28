package com.example.projectmanagement.Application;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class OpenApiConfig {
 @Bean
 OpenAPI openAPI() {
  return new OpenAPI()
   .info(new Info()
    .title("Project Management API")
    .version("v1")
    .description("Manage users and their projects through a secure, role-aware API.")
    .license(new License().name("Private API")))
   .components(new Components().addSecuritySchemes("bearerAuth",
    new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
     .description("Paste the JWT returned from **Login**. The `Bearer` prefix is added automatically.")));
 }
}
