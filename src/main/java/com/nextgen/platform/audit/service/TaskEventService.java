package com.nextgen.platform.audit.service;

import com.nextgen.platform.audit.dao.TaskEventDAO;
import com.nextgen.platform.audit.model.TaskEvent;
import com.nextgen.platform.audit.model.TaskEventStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskEventService {

    @Autowired
    private TaskEventDAO taskEventDAO;

    public void recordTaskEvent(String userId, String taskId, String eventType, Map<String, String> details) {
        TaskEvent event = new TaskEvent();
        event.setUserId(userId);
        event.setTaskId(taskId);
        event.setEventType(eventType);
        event.setEventTime(Instant.now().toString());
        event.setDetails(details);
        taskEventDAO.save(event);
        log.info("Task {} create event saved", taskId);
    }

    public void recordCreated(String userId, String taskId, Map<String, String> details) {
        recordTaskEvent(userId, taskId, TaskEventStatus.CREATED.name(), details);
    }

    public void recordUpdated(String userId, String taskId, Map<String, String> details) {
        recordTaskEvent(userId, taskId, TaskEventStatus.UPDATED.name(), details);
    }

    public void recordDeleted(String userId, String taskId, Map<String, String> details) {
        recordTaskEvent(userId, taskId, TaskEventStatus.DELETED.name(), Map.of());
    }
}

