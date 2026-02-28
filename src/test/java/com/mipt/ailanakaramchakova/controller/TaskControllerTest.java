package com.mipt.ailanakaramchakova.controller;

import com.mipt.ailanakaramchakova.model.Task;
import com.mipt.ailanakaramchakova.service.TaskService;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for TaskController endpoints.
 * Covers positive and negative scenarios for all CRUD operations.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private TaskService taskService;

  @Test
  public void testGetAll_Positive() {
    List<Task> tasks = Arrays.asList(new Task(1L, "Test", "Desc", false));
    when(taskService.findAll()).thenReturn(tasks);

    ResponseEntity<List> response = restTemplate.getForEntity("/api/tasks", List.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
  }

  @Test
  public void testGetAll_Negative() {
    when(taskService.findAll()).thenThrow(new RuntimeException("Error"));
    assertThrows(Exception.class, () -> {
      restTemplate.getForEntity("/api/tasks", List.class);
    });
  }

  @Test
  public void testGetById_Positive() {
    Task task = new Task(1L, "Test", "Desc", false);
    when(taskService.findById(1L)).thenReturn(Optional.of(task));

    ResponseEntity<Task> response = restTemplate.getForEntity("/api/tasks/1", Task.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1L, response.getBody().getId());
  }

  @Test
  public void testGetById_Negative() {
    when(taskService.findById(99L)).thenReturn(Optional.empty());

    ResponseEntity<Task> response = restTemplate.getForEntity("/api/tasks/99", Task.class);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testCreate_Positive() {
    Task task = new Task(null, "New", "Desc", false);
    when(taskService.save(any(Task.class))).thenReturn(task);

    ResponseEntity<Task> response = restTemplate.postForEntity("/api/tasks", task, Task.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(taskService, times(1)).save(any(Task.class));
  }

  @Test
  public void testCreate_Negative() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<>("", headers);

    ResponseEntity<String> response = restTemplate.exchange(
      "/api/tasks", HttpMethod.POST, entity, String.class);
    assertTrue(response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError());
  }

  @Test
  public void testUpdate_Positive() {
    Task task = new Task(1L, "Updated", "Desc", true);
    when(taskService.save(any(Task.class))).thenReturn(task);

    HttpEntity<Task> entity = new HttpEntity<>(task);
    ResponseEntity<Task> response = restTemplate.exchange(
      "/api/tasks/1", HttpMethod.PUT, entity, Task.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testUpdate_Negative() {
    Task task = new Task(1L, "Updated", "Desc", true);
    when(taskService.save(any(Task.class))).thenThrow(new RuntimeException("Error"));

    HttpEntity<Task> entity = new HttpEntity<>(task);

    ResponseEntity<Task> response = restTemplate.exchange(
      "/api/tasks/1", HttpMethod.PUT, entity, Task.class);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testDelete_Positive() {
    doNothing().when(taskService).deleteById(1L);

    ResponseEntity<Void> response = restTemplate.exchange(
      "/api/tasks/1", HttpMethod.DELETE, null, Void.class);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  public void testDelete_Negative() {
    doThrow(new RuntimeException("Error")).when(taskService).deleteById(1L);

    ResponseEntity<Void> response = restTemplate.exchange(
      "/api/tasks/1", HttpMethod.DELETE, null, Void.class);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
}
