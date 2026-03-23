package com.mipt.ailanakaramchakova.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for User Preferences using cookies.
 */
@RestController
@RequestMapping("/api/preferences")
@Tag(name = "Preferences", description = "Operations for managing user preferences")
public class PreferencesController {

  @GetMapping("/view")
  @Operation(summary = "Get view preference", description = "Returns current view preference from cookie")
  public ResponseEntity<String> getViewPreference(
    @CookieValue(value = "viewPreference", defaultValue = "detailed") String mode) {
    return ResponseEntity.ok("View mode: " + mode);
  }

  @PostMapping("/view")
  @Operation(summary = "Set view preference", description = "Sets view preference cookie")
  public ResponseEntity<Void> setViewPreference(@RequestParam String mode) {
    ResponseCookie cookie = ResponseCookie.from("viewPreference", mode)
      .httpOnly(true)
      .path("/")
      .maxAge(365 * 24 * 60 * 60)
      .sameSite("Lax")
      .build();

    return ResponseEntity.ok()
      .header(HttpHeaders.SET_COOKIE, cookie.toString())
      .build();
  }
}
