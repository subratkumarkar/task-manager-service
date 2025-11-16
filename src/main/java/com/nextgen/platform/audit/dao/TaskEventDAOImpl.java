package com.nextgen.platform.audit.dao;

import com.nextgen.platform.audit.model.TaskEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TaskEventDAOImpl implements TaskEventDAO {

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;
    DynamoDbTable<TaskEvent> taskEvents;
    @PostConstruct
    public void init() {
        taskEvents = enhancedClient.table("task_events", TableSchema.fromBean(TaskEvent.class));
    }

    public void save(TaskEvent event) {
        taskEvents.putItem(event);
    }

    public List<TaskEvent> findEventsByUser(String userId, int limit, String lastKey) {
        QueryEnhancedRequest.Builder request = QueryEnhancedRequest.builder()
                .limit(limit)
                .queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(userId)));

        if (lastKey != null) {
            request.exclusiveStartKey(Map.of(
                    "userId", AttributeValue.fromS(userId),
                    "eventTime", AttributeValue.fromS(lastKey)
            ));
        }

        return taskEvents.query(request.build())
                .items()
                .stream()
                .collect(Collectors.toList());
    }

}
