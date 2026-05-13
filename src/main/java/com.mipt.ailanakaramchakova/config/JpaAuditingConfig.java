package com.mipt.ailanakaramchakova.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration class to enable JPA Auditing.
 * Automatically populates createdAt and updatedAt fields.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

}
