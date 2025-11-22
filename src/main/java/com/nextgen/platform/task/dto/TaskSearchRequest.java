package com.nextgen.platform.task.dto;

import com.nextgen.platform.task.domain.TaskPriority;
import com.nextgen.platform.task.domain.TaskStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskSearchRequest extends BaseTaskDto{
    // Pagination
    private int limit = 20; //default page size
    private int startIndex = 0;//default start from index

    // Sorting
    private SortByField sortBy = SortByField.priority; // default sort by field
    private SortOrder sortDirection = SortOrder.DESC; //default sort order

    private LocalDateTime fromUpdatedAt;
    private LocalDateTime toUpdatedAt;

    private LocalDateTime fromDueDate;
    private LocalDateTime toDueDate;
}
