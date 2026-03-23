package com.mipt.ailanakaramchakova.controller;

import com.mipt.ailanakaramchakova.dto.AttachmentResponseDto;
import com.mipt.ailanakaramchakova.exception.TaskNotFoundException;
import com.mipt.ailanakaramchakova.model.TaskAttachment;
import com.mipt.ailanakaramchakova.service.AttachmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for AttachmentController endpoints.
 * Covers file upload, download, delete and list operations.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AttachmentControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @MockBean
  private AttachmentService attachmentService;

  @Test
  public void testUploadAttachment_Positive() throws Exception {
    MockMultipartFile file = new MockMultipartFile(
      "file",
      "test.txt",
      "text/plain",
      "test content".getBytes()
    );

    TaskAttachment attachment = new TaskAttachment(
      1L, 1L, "test.txt", "uuid_test.txt",
      "text/plain", 12L, LocalDateTime.now()
    );
    when(attachmentService.storeAttachment(anyLong(), any(MultipartFile.class)))
      .thenReturn(attachment);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("file", new org.springframework.core.io.ByteArrayResource(file.getBytes()) {
      @Override
      public String getFilename() {
        return "test.txt";
      }
    });

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

    ResponseEntity<AttachmentResponseDto> response = restTemplate.exchange(
      "/api/tasks/1/attachments",
      HttpMethod.POST,
      entity,
      AttachmentResponseDto.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("test.txt", response.getBody().getFileName());
    verify(attachmentService, times(1)).storeAttachment(anyLong(), any(MultipartFile.class));
  }

  @Test
  public void testUploadAttachment_Negative_EmptyFile() throws Exception {
    when(attachmentService.storeAttachment(anyLong(), any(MultipartFile.class)))
      .thenThrow(new IllegalArgumentException("File is empty"));

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("file", new org.springframework.core.io.ByteArrayResource(new byte[0]) {
      @Override
      public String getFilename() {
        return "empty.txt";
      }
    });

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);

    ResponseEntity<String> response = restTemplate.exchange(
      "/api/tasks/1/attachments",
      HttpMethod.POST,
      entity,
      String.class
    );

    assertTrue(
      response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError());
  }

  @Test
  public void testDownloadAttachment_Positive() throws Exception {
    TaskAttachment attachment = new TaskAttachment(
      1L, 1L, "test.txt", "uuid_test.txt",
      "text/plain", 12L, LocalDateTime.now()
    );
    when(attachmentService.getAttachment(1L)).thenReturn(attachment);
    when(attachmentService.loadAsResource(1L)).thenReturn(
      new ByteArrayResource("content".getBytes())
    );

    ResponseEntity<byte[]> response = restTemplate.getForEntity(
      "/api/attachments/1",
      byte[].class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    verify(attachmentService, times(1)).getAttachment(1L);
  }

  @Test
  public void testDownloadAttachment_Negative_NotFound() {
    when(attachmentService.getAttachment(99L))
      .thenThrow(new TaskNotFoundException(99L));

    ResponseEntity<String> response = restTemplate.getForEntity(
      "/api/attachments/99",
      String.class
    );

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  public void testDeleteAttachment_Positive() throws IOException {
    doNothing().when(attachmentService).deleteAttachment(1L);

    ResponseEntity<Void> response = restTemplate.exchange(
      "/api/attachments/1",
      HttpMethod.DELETE,
      null,
      Void.class
    );

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(attachmentService, times(1)).deleteAttachment(1L);
  }

  @Test
  public void testDeleteAttachment_Negative() throws IOException {
    doThrow(new RuntimeException("Error")).when(attachmentService).deleteAttachment(1L);

    ResponseEntity<Void> response = restTemplate.exchange(
      "/api/attachments/1",
      HttpMethod.DELETE,
      null,
      Void.class
    );

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }

  @Test
  public void testGetAttachmentsByTaskId_Positive() {
    List<TaskAttachment> attachments = Arrays.asList(
      new TaskAttachment(1L, 1L, "file1.txt", "uuid_1.txt", "text/plain", 100L,
        LocalDateTime.now()),
      new TaskAttachment(2L, 1L, "file2.txt", "uuid_2.txt", "text/plain", 200L, LocalDateTime.now())
    );
    when(attachmentService.getAttachmentsByTaskId(1L)).thenReturn(attachments);

    ResponseEntity<List> response = restTemplate.getForEntity(
      "/api/tasks/1/attachments",
      List.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(2, response.getBody().size());
  }

  @Test
  public void testGetAttachmentsByTaskId_Negative_EmptyList() {
    when(attachmentService.getAttachmentsByTaskId(1L)).thenReturn(Arrays.asList());

    ResponseEntity<List> response = restTemplate.getForEntity(
      "/api/tasks/1/attachments",
      List.class
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(0, response.getBody().size());
  }
}
