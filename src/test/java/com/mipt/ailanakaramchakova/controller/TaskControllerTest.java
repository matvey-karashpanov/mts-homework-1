package com.mipt.ailanakaramchakova.controller;

import com.mipt.ailanakaramchakova.dto.TaskDto;
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
    List<TaskDto> tasks = Arrays.asList(new TaskDto(1L, "Test", "Desc", false));
    when(taskService.findAll()).thenReturn(Arrays.asList(new com.mipt.ailanakaramchakova.model.Task(1L, "Test", "Desc", false)));

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
    TaskDto taskDto = new TaskDto(1L, "Test", "Desc", false);
    when(taskService.findById(1L)).thenReturn(Optional.of(new com.mipt.ailanakaramchakova.model.Task(1L, "Test", "Desc", false)));

    ResponseEntity<TaskDto> response = restTemplate.getForEntity("/api/tasks/1", TaskDto.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1L, response.getBody().getId());
  }

  @Test
  public void testGetById_Negative() {
    when(taskService.findById(99L)).thenReturn(Optional.empty());

    ResponseEntity<TaskDto> response = restTemplate.getForEntity("/api/tasks/99", TaskDto.class);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void testCreate_Positive() {
    TaskDto taskDto = new TaskDto(null, "New", "Desc", false);
    when(taskService.save(any())).thenReturn(new com.mipt.ailanakaramchakova.model.Task(1L, "New", "Desc", false));

    ResponseEntity<TaskDto> response = restTemplate.postForEntity("/api/tasks", taskDto, TaskDto.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(taskService, times(1)).save(any());
  }

  @Test
  public void testCreate_Negative() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> entity = new HttpEntity<>("", headers);

    ResponseEntity<String> response = restTemplate.exchange(
      "/api/tasks", HttpMethod.POST, entity, String.class);
    assertTrue(
      response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError());
  }

  @Test
  public void testUpdate_Positive() {
    TaskDto taskDto = new TaskDto(1L, "Updated", "Desc", true);
    when(taskService.save(any())).thenReturn(new com.mipt.ailanakaramchakova.model.Task(1L, "Updated", "Desc", true));

    HttpEntity<TaskDto> entity = new HttpEntity<>(taskDto);
    ResponseEntity<TaskDto> response = restTemplate.exchange(
      "/api/tasks/1", HttpMethod.PUT, entity, TaskDto.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testUpdate_Negative() {
    TaskDto taskDto = new TaskDto(1L, "Updated", "Desc", true);
    when(taskService.save(any())).thenThrow(new RuntimeException("Error"));

    HttpEntity<TaskDto> entity = new HttpEntity<>(taskDto);

    ResponseEntity<TaskDto> response = restTemplate.exchange(
      "/api/tasks/1", HttpMethod.PUT, entity, TaskDto.class);

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
