package com.nextgen.task.dao;

import com.nextgen.task.domain.Task;
import com.nextgen.task.dto.TaskSearchRequest;

import java.util.List;

public interface TaskDAO {
    Task getTaskById(Long id);
    List<Task> getTasks(TaskSearchRequest request);
    Task createTask(Task task);
    Task updateTask(Task task);
    boolean deleteTask(Long id);
    boolean activateTask(Long id);
}
