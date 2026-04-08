package com.mipt.ailanakaramchakova.config;

import com.mipt.ailanakaramchakova.repository.StubTaskRepository;
import com.mipt.ailanakaramchakova.repository.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration class.
 * Defines beans manually.
 */
@Configuration
public class AppConfig {

  /**
   * Creates a stub repository bean.
   */
  @Bean
  public TaskRepository stubTaskRepository() {
    return new StubTaskRepository();
  }
}
