package com.nextgen.platform.task.service;

import com.nextgen.platform.audit.service.TaskEventService;
import com.nextgen.platform.task.dao.TaskDAO;
import com.nextgen.platform.task.domain.Task;
import com.nextgen.platform.task.domain.TaskContainer;
import com.nextgen.platform.task.dto.TaskSearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * TaskService is responsible for handling all business logic related to Task operations.
 */
@Service
@Slf4j
public class TaskService {
    @Autowired
    private TaskDAO taskDAO;
    @Autowired
    private TaskEventService taskEventService;

   public Task createTask(@RequestBody Task task) {
       if(Objects.isNull(task)) {
           return null;
       }
       Task createdTask = taskDAO.createTask(task);
       if(Objects.nonNull(createdTask)) {
           taskEventService.recordCreated(task.getAssignedTo().toString(),
                   createdTask.getId().toString(),
                   createTaskDetail(createdTask));
       }
       return createdTask;
    }

    public Task updateTask(Task task) {
       if(Objects.isNull(task)){
          return null;
       }
        Task updatedTask =  taskDAO.updateTask(task);
        if(Objects.nonNull(updatedTask)) {
            taskEventService.recordUpdated(task.getAssignedTo().toString(),
                    updatedTask.getId().toString(),
                    createTaskDetail(updatedTask));
        }
        return updatedTask;
    }

    public Task getTaskById(Long id) {
      if(Objects.isNull(id)){
          return null;
      }
       return  taskDAO.getTaskById(id);
    }

    public TaskContainer searchTasks(TaskSearchRequest request, Long userId) {
        if(Objects.isNull(userId) || Objects.isNull(request)) {
            return null;
        }
       return taskDAO.searchTasks(request, userId);
    }


    public boolean deleteTask(Long id, Long userId) {
        log.debug("Delete task with id {}", id);
        //get the task to publish audit events
        Task task = taskDAO.getTaskById(id);
        if(Objects.isNull(task)) {
            return false;
        }
        boolean isDeleted = taskDAO.deleteTask(id);
        if(isDeleted) {
            taskEventService.recordDeleted(userId.toString(), id.toString(), createTaskDetail(task));
        }
        return isDeleted;
    }

    private static Map<String, String> createTaskDetail(Task createdTask) {
        return Map.of("title", createdTask.getTitle(),
                "description", createdTask.getDescription(),
                "status", Objects.nonNull(createdTask.getStatus())?createdTask.getStatus().toString():"",
                "priority", Objects.nonNull(createdTask.getPriority())?createdTask.getPriority().toString():"",
                "dueDate", Objects.nonNull(createdTask.getDueDate())?createdTask.getDueDate().toString():""
        );
    }
}
