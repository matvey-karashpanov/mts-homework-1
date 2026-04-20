package com.mipt.ailanakaramchakova.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mipt.ailanakaramchakova.dto.ProblemDetails;
import com.mipt.ailanakaramchakova.dto.TaskCreateRequest;
import com.mipt.ailanakaramchakova.dto.TaskListDto;
import com.mipt.ailanakaramchakova.dto.TaskResponseDto;
import com.mipt.ailanakaramchakova.exception.ExternalApiException;
import com.mipt.ailanakaramchakova.exception.TaskNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class ExternalTasksClient {

    private static final Logger log = LoggerFactory.getLogger(ExternalTasksClient.class);
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public ExternalTasksClient(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    public TaskResponseDto createTask(TaskCreateRequest request) {
        return restClient.post()
          .uri("/external/v1/tasks")
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
          .body(request)
          .retrieve()
          .body(TaskResponseDto.class);
    }

    public TaskResponseDto getTaskById(Long identifier) {
        try {
            return restClient.get()
              .uri("/external/v1/tasks/{id}", identifier)
              .accept(MediaType.APPLICATION_JSON)
              .retrieve()
              .body(TaskResponseDto.class);
        } catch (HttpClientErrorException.NotFound exception) {
            ProblemDetails problemDetails = parseProblemDetails(exception);
            throw new TaskNotFoundException(
              problemDetails != null ? problemDetails.detail() : "Task not found");
        } catch (RestClientResponseException exception) {
            handleServerError(exception);
            throw new ExternalApiException("Unexpected client error", exception);
        }
    }

    public List<TaskListDto> listTasks(Boolean isCompleted, Integer limitValue) {
        try {
            return restClient.get()
              .uri(uriComponentsBuilder -> uriComponentsBuilder
                .path("/external/v1/tasks")
                .queryParamIfPresent("completed", java.util.Optional.ofNullable(isCompleted))
                .queryParam("limit", limitValue)
                .build())
              .accept(MediaType.APPLICATION_JSON)
              .retrieve()
              .body(new ParameterizedTypeReference<List<TaskListDto>>() {
              });
        } catch (HttpServerErrorException exception) {
            handleServerError(exception);
            throw new ExternalApiException("Server error on list tasks", exception);
        }
    }

    public void deleteTask(Long identifier) {
        restClient.delete()
          .uri("/external/v1/tasks/{id}", identifier)
          .retrieve()
          .toBodilessEntity();
    }

    private ProblemDetails parseProblemDetails(HttpClientErrorException exception) {
        try {
            byte[] bodyBytes = exception.getResponseBodyAsByteArray();
            if (bodyBytes == null || bodyBytes.length == 0) {
                return null;
            }
            return objectMapper.readValue(bodyBytes, ProblemDetails.class);
        } catch (Exception parsingException) {
            return null;
        }
    }

    private void handleServerError(RestClientResponseException exception) {
        HttpStatusCode statusCode = exception.getStatusCode();
        if (statusCode.is5xxServerError()) {
            String safeBody = getSafeBody(exception);
            log.error("External API 5xx error: status={}, body={}", statusCode.value(), safeBody);
            throw new ExternalApiException("Server error: " + statusCode.value(), exception);
        }
    }

    private String getSafeBody(RestClientResponseException exception) {
        byte[] bodyBytes = exception.getResponseBodyAsByteArray();
        if (bodyBytes == null || bodyBytes.length == 0) {
            return "";
        }
        String bodyString = new String(bodyBytes, StandardCharsets.UTF_8);
        if (bodyString.length() > 200) {
            return bodyString.substring(0, 200) + "...";
        }
        return bodyString;
    }
}
