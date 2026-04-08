package com.mipt.ailanakaramchakova.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration for Swagger documentation.
 */
@Configuration
public class OpenApiConfig {

  @Value("${app.api-version}")
  private String apiVersion;

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
      .info(new Info()
        .title("To-Do List API")
        .version(apiVersion)
        .description("API for managing tasks and attachments")
        .contact(new Contact()
          .name("API Support")
          .email("support@example.com"))
        .license(new License()
          .name("Apache 2.0")
          .url("https://www.apache.org/licenses/LICENSE-2.0")));
  }
}
