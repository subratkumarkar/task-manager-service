package com.nextgen.platform.task.controller;

import java.util.Objects;
import com.nextgen.platform.security.JwtService;
import com.nextgen.platform.task.controller.mapper.TaskMapper;
import com.nextgen.platform.task.domain.Task;
import com.nextgen.platform.task.domain.TaskContainer;
import com.nextgen.platform.task.dto.TaskRequest;
import com.nextgen.platform.task.dto.TaskResponse;
import com.nextgen.platform.task.dto.TaskSearchRequest;
import com.nextgen.platform.task.dto.TaskSearchResponse;
import com.nextgen.platform.task.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/tasks")
@Slf4j
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private JwtService jwtService;

    /**
     * Creates a new Task.
     * @param taskRequest TaskRequest object containing the details to create.
     * @param authHeader  contains user token
     * @return ResponseEntity containing the created Task, or 400 Bad Request if creation fails.
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest taskRequest,
                                           @RequestHeader("Authorization") String authHeader) {
        log.debug("Creating task {}", taskRequest);
        if(Objects.isNull(taskRequest) || StringUtils.isEmpty(authHeader)
               || StringUtils.isEmpty(taskRequest.getTitle())) {
            return ResponseEntity.badRequest().build();
        }
        Long userId = jwtService.getUserIdFromAuthHeader(authHeader);
        if(Objects.isNull(userId)) {
            return ResponseEntity.badRequest().build();
        }
        Task created = taskService.createTask(TaskMapper.toEntity(taskRequest, userId));
        if (created == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(TaskMapper.toResponse(created));
    }

    /**
     * Updates an existing Task by ID.
     * @param taskRequest  the task to update.
     * @param authHeader contains user info
     * @return ResponseEntity containing the updated Task, or 404 Not Found if task doesn't exist.
     */
    @PutMapping
    public ResponseEntity<TaskResponse> updateTask(@RequestBody TaskRequest taskRequest,
                                                   @RequestHeader("Authorization") String authHeader) {
        if(Objects.isNull(taskRequest) || StringUtils.isEmpty(authHeader)
                || StringUtils.isEmpty(taskRequest.getTitle())) {
            return ResponseEntity.badRequest().build();
        }
        Long userId = jwtService.getUserIdFromAuthHeader(authHeader);
        if(Objects.isNull(userId)) {
            return ResponseEntity.badRequest().build();
        }
        Task updated = taskService.updateTask(TaskMapper.toEntity(taskRequest, userId));
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(TaskMapper.toResponse(updated));
    }

    /**
     * Retrieves a Task by its ID.
     * @param id The ID of the task to retrieve.
     * @return ResponseEntity containing the Taskx
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        log.info("Getting task with id {}", id);
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(TaskMapper.toResponse(task));
    }

    /**
     * Searches for tasks based on filters, pagination, and sorting criteria.
     * @param request TaskSearchRequest object containing search parameters:
     * @return ResponseEntity containing a list of matching Task objects.
     */
    @PostMapping("/search")
    public ResponseEntity<TaskSearchResponse> searchTasks(@RequestBody(required = false) TaskSearchRequest request,
                                                          @RequestHeader("Authorization") String authHeader) {
        log.debug("Searching for tasks {}", request);
        Long userId = jwtService.getUserIdFromAuthHeader(authHeader);
        if(Objects.isNull(userId)) {
            return ResponseEntity.badRequest().build();
        }
        TaskContainer taskContainer = taskService.searchTasks(request, userId);
        return ResponseEntity.ok(TaskMapper.toSearchResponse(taskContainer));
    }

    /**
     * Deletes a Task by its ID.
     * @param id The ID of the task to delete.
     * @return ResponseEntity with no content (204) after deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTask(@PathVariable Long id,
                                              @RequestHeader("Authorization") String authHeader) {
        log.debug("Delete task with id {}", id);
        Long userId = jwtService.getUserIdFromAuthHeader(authHeader);
        if(Objects.isNull(userId)) {
            return ResponseEntity.badRequest().build();
        }
        boolean isTaskDeleted = taskService.deleteTask(id, userId);
        return isTaskDeleted?ResponseEntity.noContent().build():ResponseEntity.notFound().build();
    }

}
