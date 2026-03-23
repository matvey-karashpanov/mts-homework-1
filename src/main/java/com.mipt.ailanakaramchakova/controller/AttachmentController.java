package com.mipt.ailanakaramchakova.controller;

import com.mipt.ailanakaramchakova.dto.AttachmentResponseDto;
import com.mipt.ailanakaramchakova.model.TaskAttachment;
import com.mipt.ailanakaramchakova.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST Controller for Task Attachments.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Attachment Management", description = "Operations for managing task attachments")
public class AttachmentController {

  private final AttachmentService attachmentService;

  public AttachmentController(AttachmentService attachmentService) {
    this.attachmentService = attachmentService;
  }

  @PostMapping("/tasks/{taskId}/attachments")
  @Operation(summary = "Upload attachment", description = "Uploads a file attachment for a task")
  @ApiResponse(responseCode = "200", description = "File uploaded successfully")
  public ResponseEntity<AttachmentResponseDto> uploadAttachment(
    @PathVariable Long taskId,
    @RequestParam("file") MultipartFile file) throws IOException {
    TaskAttachment attachment = attachmentService.storeAttachment(taskId, file);
    AttachmentResponseDto response = new AttachmentResponseDto(
      attachment.getId(),
      attachment.getFileName(),
      attachment.getSize(),
      attachment.getUploadedAt()
    );
    return ResponseEntity.ok(response);
  }

  @GetMapping("/attachments/{attachmentId}")
  @Operation(summary = "Download attachment", description = "Downloads a file attachment")
  @ApiResponse(responseCode = "200", description = "File downloaded successfully")
  @ApiResponse(responseCode = "404", description = "Attachment not found")
  public ResponseEntity<Resource> downloadAttachment(@PathVariable Long attachmentId)
    throws IOException {
    Resource resource = attachmentService.loadAsResource(attachmentId);
    TaskAttachment attachment = attachmentService.getAttachment(attachmentId);

    return ResponseEntity.ok()
      .header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + attachment.getFileName() + "\"")
      .contentType(MediaType.parseMediaType(attachment.getContentType()))
      .body(resource);
  }

  @DeleteMapping("/attachments/{attachmentId}")
  @Operation(summary = "Delete attachment", description = "Deletes a file attachment")
  @ApiResponse(responseCode = "204", description = "Attachment deleted successfully")
  @ApiResponse(responseCode = "404", description = "Attachment not found")
  public ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId) throws IOException {
    attachmentService.deleteAttachment(attachmentId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/tasks/{taskId}/attachments")
  @Operation(summary = "Get task attachments", description = "Returns all attachments for a task")
  @ApiResponse(responseCode = "200", description = "Attachments retrieved successfully")
  public ResponseEntity<List<AttachmentResponseDto>> getAttachmentsByTaskId(
    @PathVariable Long taskId) {
    List<TaskAttachment> attachments = attachmentService.getAttachmentsByTaskId(taskId);
    List<AttachmentResponseDto> response = new ArrayList<>();
    for (TaskAttachment attachment : attachments) {
      response.add(new AttachmentResponseDto(
        attachment.getId(),
        attachment.getFileName(),
        attachment.getSize(),
        attachment.getUploadedAt()
      ));
    }
    return ResponseEntity.ok(response);
  }
}
