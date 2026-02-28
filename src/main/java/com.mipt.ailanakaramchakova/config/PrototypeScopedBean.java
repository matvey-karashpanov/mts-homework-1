package com.mipt.ailanakaramchakova.config;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * Bean with Prototype scope.
 * New instance every time it is requested.
 */
@Component
@Scope("prototype")
public class PrototypeScopedBean {
  private final String uuid = UUID.randomUUID().toString();

  public String getUuid() {
    return uuid;
  }
}
