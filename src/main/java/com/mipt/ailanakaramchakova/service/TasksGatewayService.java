package com.mipt.ailanakaramchakova.service;

import com.mipt.ailanakaramchakova.client.ExternalTasksClient;
import com.mipt.ailanakaramchakova.dto.TaskCreateRequest;
import com.mipt.ailanakaramchakova.dto.TaskListDto;
import com.mipt.ailanakaramchakova.dto.TaskResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TasksGatewayService {

    private final ExternalTasksClient externalTasksClient;

    public TasksGatewayService(ExternalTasksClient externalTasksClient) {
        this.externalTasksClient = externalTasksClient;
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "getTaskFallback")
    public TaskResponseDto getTask(Long identifier) {
        return externalTasksClient.getTaskById(identifier);
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "listTasksFallback")
    public List<TaskListDto> listTasks(Boolean isCompleted, Integer limitValue) {
        return externalTasksClient.listTasks(isCompleted, limitValue);
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "createTaskFallback")
    public TaskResponseDto createTask(TaskCreateRequest request) {
        return externalTasksClient.createTask(request);
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "deleteTaskFallback")
    public void deleteTask(Long identifier) {
        externalTasksClient.deleteTask(identifier);
    }

    public TaskResponseDto getTaskFallback(Long identifier, Throwable throwable) {
        return new TaskResponseDto(null, "Fallback Task", "Service temporarily unavailable", false);
    }

    public List<TaskListDto> listTasksFallback(Boolean isCompleted, Integer limitValue,
      Throwable throwable) {
        return List.of();
    }

    public TaskResponseDto createTaskFallback(TaskCreateRequest request, Throwable throwable) {
        return new TaskResponseDto(null, request.title(), request.description(), false);
    }

    public void deleteTaskFallback(Long identifier, Throwable throwable) {
    }
}
