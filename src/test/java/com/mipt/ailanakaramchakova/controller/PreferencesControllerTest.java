package com.mipt.ailanakaramchakova.controller;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Tests for PreferencesController endpoints. Covers cookie-based user preferences functionality.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PreferencesControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void testGetViewPreference_Positive() {
    ResponseEntity<String> response = restTemplate.getForEntity(
      "/api/preferences/view",
      String.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().contains("View mode:"));
  }

  @Test
  public void testGetViewPreference_WithCookie() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Cookie", "viewPreference=compact");
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange(
      "/api/preferences/view",
      HttpMethod.GET,
      entity,
      String.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("View mode: compact", response.getBody());
  }

  @Test
  public void testSetViewPreference_Positive_Compact() {
    ResponseEntity<Void> response = restTemplate.postForEntity(
      "/api/preferences/view?mode=compact",
      null,
      Void.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
    assertTrue(
      response.getHeaders().getFirst(HttpHeaders.SET_COOKIE).contains("viewPreference=compact"));
  }

  @Test
  public void testSetViewPreference_Positive_Detailed() {
    ResponseEntity<Void> response = restTemplate.postForEntity(
      "/api/preferences/view?mode=detailed",
      null,
      Void.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
    assertTrue(
      response.getHeaders().getFirst(HttpHeaders.SET_COOKIE).contains("viewPreference=detailed"));
  }
}
