package com.nextgen.platform.task.controller.mapper;

import com.nextgen.platform.task.domain.Task;
import com.nextgen.platform.task.domain.TaskContainer;
import com.nextgen.platform.task.dto.TaskRequest;
import com.nextgen.platform.task.dto.TaskResponse;
import com.nextgen.platform.task.dto.TaskSearchResponse;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TaskMapper {

    public static Task toEntity(TaskRequest req, Long userId) {
        Task task = new Task();
        task.setAssignedTo(userId);
        task.setId(req.getId());
        task.setTitle(req.getTitle());
        task.setDescription(req.getDescription());
        task.setPriority(req.getPriority());
        task.setDueDate(req.getDueDate());
        task.setStatus(req.getStatus());
        return task;
    }

    public static TaskResponse toResponse(Task task) {
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setId(task.getId());
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDescription(task.getDescription());
        taskResponse.setPriority(task.getPriority());
        taskResponse.setDueDate(task.getDueDate());
        taskResponse.setStatus(task.getStatus());
        taskResponse.setUpdatedAt(task.getUpdatedAt());
        return taskResponse;
    }

    public static TaskSearchResponse toSearchResponse(TaskContainer taskContainer) {
        TaskSearchResponse taskSearchResponse = new TaskSearchResponse();
        if(Objects.nonNull(taskContainer)){
            if(!CollectionUtils.isEmpty(taskContainer.getItems())){
                taskSearchResponse.setItems(taskContainer.getItems().stream().map(task -> {
                    return toResponse(task);
                }).collect(Collectors.toList()));
            }
            taskSearchResponse.setTotalCount(taskContainer.getTotalCount());
        };
        return taskSearchResponse;
    }
}
