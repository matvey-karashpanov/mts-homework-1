package com.mipt.ailanakaramchakova.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mipt.ailanakaramchakova.config.RequestScopedBean;
import com.mipt.ailanakaramchakova.dto.TaskCreateDto;
import com.mipt.ailanakaramchakova.dto.TaskResponseDto;
import com.mipt.ailanakaramchakova.exception.TaskNotFoundException;
import com.mipt.ailanakaramchakova.model.Priority;
import com.mipt.ailanakaramchakova.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Slice tests for TaskController using MockMvc. Tests HTTP endpoints, request validation and JSON
 * mapping without loading the full application context.
 */
@WebMvcTest(TaskController.class)
@ActiveProfiles("test")
@ImportAutoConfiguration(exclude = {JpaRepositoriesAutoConfiguration.class,
  HibernateJpaAutoConfiguration.class})
public class TaskControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @MockBean
    private RequestScopedBean requestScopedBean;

    @Test
    public void givenValidTaskDto_whenCreateTask_thenReturns201AndJsonResponse() throws Exception {
        TaskCreateDto createDto = new TaskCreateDto();
        createDto.setTitle("New Task");
        createDto.setDescription("Task Description");
        createDto.setPriority(Priority.HIGH);
        createDto.setDueDate(LocalDate.now().plusDays(7));
        createDto.setTags(new HashSet<>(Arrays.asList("important", "urgent")));

        TaskResponseDto responseDto = new TaskResponseDto(
          1L, "New Task", "Task Description", false,
          LocalDateTime.now(), LocalDate.now().plusDays(7),
          Priority.HIGH, new HashSet<>(Arrays.asList("important", "urgent"))
        );

        when(taskService.create(any(TaskCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createDto)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.title").value("New Task"))
          .andExpect(jsonPath("$.priority").value("HIGH"));

        verify(taskService, times(1)).create(any(TaskCreateDto.class));
    }

    @Test
    public void givenExistingTaskId_whenGetTaskById_thenReturns200AndJsonResponse()
      throws Exception {
        Long taskId = 1L;
        TaskResponseDto taskDto = new TaskResponseDto(
          taskId, "Test Task", "Test Description", false,
          LocalDateTime.now(), LocalDate.now().plusDays(7),
          Priority.MEDIUM, new HashSet<>(Arrays.asList("test"))
        );

        when(taskService.findById(taskId)).thenReturn(taskDto);

        mockMvc.perform(get("/api/tasks/{id}", taskId))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(1))
          .andExpect(jsonPath("$.title").value("Test Task"))
          .andExpect(jsonPath("$.description").value("Test Description"))
          .andExpect(jsonPath("$.priority").value("MEDIUM"))
          .andExpect(jsonPath("$.tags").isArray())
          .andExpect(jsonPath("$.tags[0]").value("test"));

        verify(taskService, times(1)).findById(taskId);
    }

    @Test
    public void givenNonExistingTaskId_whenGetTaskById_thenReturns404() throws Exception {
        Long taskId = 999L;
        when(taskService.findById(taskId)).thenThrow(new TaskNotFoundException(taskId));

        mockMvc.perform(get("/api/tasks/{id}", taskId))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.status").value(404))
          .andExpect(jsonPath("$.error").value("NOT_FOUND"));

        verify(taskService, times(1)).findById(taskId);
    }

    @Test
    public void givenInvalidTaskDto_whenCreateTask_thenReturns400ValidationError()
      throws Exception {
        TaskCreateDto invalidDto = new TaskCreateDto();
        invalidDto.setTitle("");
        invalidDto.setPriority(Priority.HIGH);

        mockMvc.perform(post("/api/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidDto)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.status").value(400))
          .andExpect(jsonPath("$.error").value("VALIDATION_FAILED"));

        verify(taskService, times(0)).create(any(TaskCreateDto.class));
    }
}
