package com.mipt.ailanakaramchakova;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main application class. Starts the Spring Boot application and enables AspectJ auto proxying.
 */
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableJpaAuditing
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
