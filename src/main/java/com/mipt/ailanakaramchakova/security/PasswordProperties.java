package com.mipt.ailanakaramchakova.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.password")
public record PasswordProperties(String pepper) {

}
