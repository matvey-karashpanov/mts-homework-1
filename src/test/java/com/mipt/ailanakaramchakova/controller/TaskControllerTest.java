package com.mipt.ailanakaramchakova.controller;

import com.mipt.ailanakaramchakova.dto.ErrorResponse;
import com.mipt.ailanakaramchakova.dto.TaskCreateDto;
import com.mipt.ailanakaramchakova.dto.TaskResponseDto;
import com.mipt.ailanakaramchakova.dto.TaskUpdateDto;
import com.mipt.ailanakaramchakova.exception.TaskNotFoundException;
import com.mipt.ailanakaramchakova.model.Priority;
import com.mipt.ailanakaramchakova.service.TaskService;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    List<TaskResponseDto> tasks = Arrays.asList(
      new TaskResponseDto(1L, "Test", "Desc", false,
        LocalDateTime.now(), null, Priority.MEDIUM, new HashSet<>())
    );
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
    TaskResponseDto taskDto = new TaskResponseDto(1L, "Test", "Desc", false,
      LocalDateTime.now(), null, Priority.MEDIUM, new HashSet<>());
    when(taskService.findById(1L)).thenReturn(taskDto);

    ResponseEntity<TaskResponseDto> response =
      restTemplate.getForEntity("/api/tasks/1", TaskResponseDto.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1L, response.getBody().getId());
  }

  @Test
  public void testGetById_Negative() {
    when(taskService.findById(99L)).thenThrow(new TaskNotFoundException(99L));

    ResponseEntity<ErrorResponse> response =
      restTemplate.getForEntity("/api/tasks/99", ErrorResponse.class);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("NOT_FOUND", response.getBody().getError());
    assertTrue(response.getBody().getMessage().contains("99"));
  }

  @Test
  public void testCreate_Positive() {
    TaskCreateDto createDto = new TaskCreateDto();
    createDto.setTitle("New Task");
    createDto.setDescription("Description");
    createDto.setPriority(Priority.HIGH);
    createDto.setTags(new HashSet<>());

    TaskResponseDto createdDto = new TaskResponseDto(1L, "New Task", "Description", false,
      LocalDateTime.now(), null, Priority.HIGH, new HashSet<>());
    when(taskService.create(any())).thenReturn(createdDto);

    ResponseEntity<TaskResponseDto> response =
      restTemplate.postForEntity("/api/tasks", createDto, TaskResponseDto.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(taskService, times(1)).create(any());
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
    TaskUpdateDto updateDto = new TaskUpdateDto();
    updateDto.setTitle("Updated Task");
    updateDto.setDescription("New Description");
    updateDto.setCompleted(true);
    updateDto.setPriority(Priority.LOW);
    updateDto.setTags(new HashSet<>());

    TaskResponseDto updatedDto = new TaskResponseDto(1L, "Updated Task", "New Description", true,
      LocalDateTime.now(), null, Priority.LOW, new HashSet<>());
    when(taskService.update(anyLong(), any())).thenReturn(updatedDto);

    HttpEntity<TaskUpdateDto> entity = new HttpEntity<>(updateDto);
    ResponseEntity<TaskResponseDto> response = restTemplate.exchange(
      "/api/tasks/1", HttpMethod.PUT, entity, TaskResponseDto.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void testUpdate_Negative() {
    TaskUpdateDto updateDto = new TaskUpdateDto();
    updateDto.setTitle("Updated Task");

    when(taskService.update(anyLong(), any())).thenThrow(new RuntimeException("Error"));

    HttpEntity<TaskUpdateDto> entity = new HttpEntity<>(updateDto);

    ResponseEntity<TaskResponseDto> response = restTemplate.exchange(
      "/api/tasks/1", HttpMethod.PUT, entity, TaskResponseDto.class);

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
