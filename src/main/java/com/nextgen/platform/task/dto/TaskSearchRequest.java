package com.nextgen.platform.task.dto;

import com.nextgen.platform.task.domain.TaskPriority;
import com.nextgen.platform.task.domain.TaskStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TaskSearchRequest {
    //Must have fields. Task search is by logged on user
    private String assignedTo;
    // Pagination
    private int limit = 20; //default page size
    private int startIndex = 0;//default start from index

    // Sorting
    private SortByField sortBy = SortByField.priority; // default sort by field
    private SortOrder sortDirection = SortOrder.DESC; //default sort order

    //Filter fields
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime dueDate;
    private LocalDateTime fromUpdatedAt;
    private LocalDateTime toUpdatedAt;
}
