package com.nextgen.platform.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nextgen.platform.task.domain.TaskPriority;
import com.nextgen.platform.task.domain.TaskStatus;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseTaskDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
}
