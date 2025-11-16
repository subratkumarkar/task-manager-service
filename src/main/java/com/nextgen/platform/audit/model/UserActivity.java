package com.nextgen.platform.audit.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserActivity {
    private String activityTime;
    private String taskId;
    private String eventType;

    private String title;
    private String description;
    private String priority;
    private String status;
    private String dueDate;
}

