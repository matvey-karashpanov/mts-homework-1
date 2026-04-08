package com.mipt.ailanakaramchakova.config;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.UUID;

/**
 * Bean with Request scope.
 * New instance for every HTTP request.
 */
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestScopedBean {

  private final String requestId = UUID.randomUUID().toString();
  private final Instant startTime = Instant.now();

  public String getRequestId() {
    return requestId;
  }

  public Instant getStartTime() {
    return startTime;
  }
}
