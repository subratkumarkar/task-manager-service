package com.nextgen.platform.audit.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;
import java.util.Map;

@DynamoDbBean
public class TaskEvent {

    private String userId;      // PK
    private String eventTime;   // SK = timestamp
    private String eventType;   // CREATED / UPDATED / DELETED / ACTIVATED
    private String taskId;
    private Map<String, String> details;

    @DynamoDbPartitionKey
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    @DynamoDbSortKey
    public String getEventTime() { return eventTime; }
    public void setEventTime(String eventTime) { this.eventTime = eventTime; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    @DynamoDbAttribute("details")
    public Map<String, String> getDetails() { return details; }
    public void setDetails(Map<String, String> details) { this.details = details; }
}

