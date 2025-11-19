package com.nextgen.platform.audit.dao;

import com.nextgen.platform.audit.model.TaskEvent;

import java.util.List;

public interface TaskEventDAO {
    void save(TaskEvent event);
    List<TaskEvent> findEventsByUser(String userId, int limit, String lastKey);
    long countEventsByUser(String userId);
}
