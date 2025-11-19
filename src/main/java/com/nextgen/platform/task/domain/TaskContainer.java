package com.nextgen.platform.task.domain;

import lombok.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TaskContainer {
    private long totalCount;
    private List<Task> items;
}
