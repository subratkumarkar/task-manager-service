package com.nextgen.task.service;

import com.nextgen.task.dao.TaskDAO;
import com.nextgen.task.domain.Task;
import com.nextgen.task.dto.TaskSearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * TaskService is responsible for handling all business logic related to Task operations.
 */
@Service
@Slf4j
public class TaskService {
    @Autowired
    private TaskDAO taskDAO;

   public Task createTask(@RequestBody Task task) {
       if(Objects.isNull(task)) {
           return null;
       }
       return taskDAO.createTask(task);
    }

    public Task updateTask(Long id, Task task) {
       if(Objects.isNull(id) || Objects.isNull(task)){
          return null;
       }
        task.setId(id);
        return taskDAO.updateTask(task);
    }

    public Task getTaskById(Long id) {
      if(Objects.isNull(id)){
          return null;
      }
       return  taskDAO.getTaskById(id);
    }

    public List<Task> getTasks(TaskSearchRequest request) {
        if(Objects.isNull(request)) {
            return Collections.emptyList();
        }
        if(!StringUtils.hasText(request.getAssignedTo())) {
            throw new InvalidParameterException("Missing input parameter: assignedTo");
        }
       return taskDAO.getTasks(request);
    }


    public boolean deleteTask(Long id) {
        log.debug("Delete task with id {}", id);
        return taskDAO.deleteTask(id);
    }

    public boolean activateTask(Long id) {
        log.debug("Activate task with id {}", id);
        return taskDAO.activateTask(id);
    }
}
