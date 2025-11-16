package com.nextgen.platform.task.controller;

import java.util.List;

import com.nextgen.platform.task.domain.Task;
import com.nextgen.platform.task.dto.TaskSearchRequest;
import com.nextgen.platform.task.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/tasks")
@Slf4j
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * Creates a new Task.
     * @param task Task object containing the details to create.
     * @return ResponseEntity containing the created Task, or 400 Bad Request if creation fails.
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        log.debug("Creating task {}", task);
        Task created = taskService.createTask(task);
        if (created == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(created);
    }

    /**
     * Updates an existing Task by ID.
     * @param id   The ID of the task to update.
     * @param task Task object containing updated details.
     * @return ResponseEntity containing the updated Task, or 404 Not Found if task doesn't exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        log.debug("Updating task with id {}", id);
        Task updated = taskService.updateTask(id, task);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    /**
     * Retrieves a Task by its ID.
     * @param id The ID of the task to retrieve.
     * @return ResponseEntity containing the Taskx
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        log.info("Getting task with id {}", id);
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    /**
     * Searches for tasks based on filters, pagination, and sorting criteria.
     * @param request TaskSearchRequest object containing search parameters:
     * @return ResponseEntity containing a list of matching Task objects.
     */
    @PostMapping("/search")
    public ResponseEntity<List<Task>> getTasks(@RequestBody TaskSearchRequest request) {
        log.debug("Searching for tasks {}", request);
        List<Task> tasks = taskService.getTasks(request);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Deletes a Task by its ID.
     * @param id The ID of the task to delete.
     * @return ResponseEntity with no content (204) after deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTask(@PathVariable Long id) {
        log.debug("Delete task with id {}", id);
        boolean isTaskDeleted = taskService.deleteTask(id);
        return isTaskDeleted?ResponseEntity.noContent().build():ResponseEntity.notFound().build();
    }

    /**
     * Activates a Task by its ID.
     *
     * @param id The ID of the task to activate.
     * @return ResponseEntity with status 200 OK after activation.
     */
    @PostMapping("/{id}/activate")
    public ResponseEntity<Boolean> activateTask(@PathVariable Long id) {
        log.debug("Activate task with id {}", id);
        boolean isActivated =taskService.activateTask(id);
        return isActivated?ResponseEntity.ok().build():ResponseEntity.notFound().build();
    }
}
