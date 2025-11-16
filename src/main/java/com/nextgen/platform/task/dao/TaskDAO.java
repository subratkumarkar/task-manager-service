package com.nextgen.platform.task.dao;

import com.nextgen.platform.task.domain.Task;
import com.nextgen.platform.task.dto.TaskSearchRequest;

import java.util.List;

public interface TaskDAO {
    Task getTaskById(Long id);
    List<Task> getTasks(TaskSearchRequest request);
    Task createTask(Task task);
    Task updateTask(Task task);
    boolean deleteTask(Long id);
    boolean activateTask(Long id);
}
