package com.mipt.ailanakaramchakova.controller;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mipt.ailanakaramchakova.dto.TaskResponseDto;
import com.mipt.ailanakaramchakova.service.FavoritesService;
import com.mipt.ailanakaramchakova.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Tests for FavoritesController endpoints.
 * Covers session-based favorite tasks functionality.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FavoritesControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private FavoritesService favoritesService;

  @MockBean
  private TaskService taskService;

  @Test
  public void testAddToFavorites_Positive() {
    doNothing().when(favoritesService).addToFavorites(1L);

    ResponseEntity<Void> response = restTemplate.exchange(
      "/api/favorites/1",
      HttpMethod.POST,
      null,
      Void.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(favoritesService, times(1)).addToFavorites(1L);
  }

  @Test
  public void testAddToFavorites_Negative_InvalidId() {
    doThrow(new IllegalArgumentException("Invalid task ID")).when(favoritesService)
      .addToFavorites(-1L);

    ResponseEntity<Void> response = restTemplate.exchange(
      "/api/favorites/-1",
      HttpMethod.POST,
      null,
      Void.class
    );

    assertTrue(
      response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError());
  }

  @Test
  public void testRemoveFromFavorites_Positive() {
    doNothing().when(favoritesService).removeFromFavorites(1L);

    ResponseEntity<Void> response = restTemplate.exchange(
      "/api/favorites/1",
      HttpMethod.DELETE,
      null,
      Void.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(favoritesService, times(1)).removeFromFavorites(1L);
  }

  @Test
  public void testRemoveFromFavorites_Negative() {
    doThrow(new RuntimeException("Error")).when(favoritesService).removeFromFavorites(1L);

    ResponseEntity<Void> response = restTemplate.exchange(
      "/api/favorites/1",
      HttpMethod.DELETE,
      null,
      Void.class
    );

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testGetFavorites_Positive() {
    List<Long> favoriteIds = Arrays.asList(1L, 2L);
    when(favoritesService.getFavorites()).thenReturn(favoriteIds);

    TaskResponseDto task1 = new TaskResponseDto(
      1L, "Task 1", "Desc 1", false,
      LocalDateTime.now(), null, null, null
    );
    TaskResponseDto task2 = new TaskResponseDto(
      2L, "Task 2", "Desc 2", true,
      LocalDateTime.now(), null, null, null
    );
    when(taskService.findById(1L)).thenReturn(task1);
    when(taskService.findById(2L)).thenReturn(task2);

    ResponseEntity<List> response = restTemplate.getForEntity(
      "/api/favorites",
      List.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(2, response.getBody().size());
  }

  @Test
  public void testGetFavorites_Negative_EmptyList() {
    when(favoritesService.getFavorites()).thenReturn(Arrays.asList());

    ResponseEntity<List> response = restTemplate.getForEntity(
      "/api/favorites",
      List.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(0, response.getBody().size());
  }
}
