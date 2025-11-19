package com.nextgen.platform.audit.dao;

import com.nextgen.platform.audit.model.TaskEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.Select;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TaskEventDAOImpl implements TaskEventDAO {

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;
    @Autowired
    private DynamoDbClient dynamoDbClient;
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
                .scanIndexForward(false)
                .queryConditional(QueryConditional.keyEqualTo(k -> k.partitionValue(userId))
                );

        if (lastKey != null) {
            request.exclusiveStartKey(Map.of("userId", AttributeValue.fromS(userId),
                    "eventTime", AttributeValue.fromS(lastKey)
            ));
        }
        return taskEvents.query(request.build()).items() .stream() .collect(Collectors.toList());
    }

    public long countEventsByUser(String userId) {
        long total = 0;
        String lastKey = null;

        do {
            QueryRequest.Builder builder = QueryRequest.builder()
                    .tableName("task_events").keyConditionExpression("userId = :uid")
                    .expressionAttributeValues(Map.of(":uid", AttributeValue.fromS(userId)
                    )).select(Select.COUNT).limit(1000);

            if (lastKey != null) {
                builder.exclusiveStartKey(Map.of("userId", AttributeValue.fromS(userId),
                        "eventTime", AttributeValue.fromS(lastKey)
                ));
            }
            QueryRequest request = builder.build();
            QueryResponse response = dynamoDbClient.query(request);

            total += response.count();
            if (response.lastEvaluatedKey() != null && response.lastEvaluatedKey().containsKey("eventTime")) {
                lastKey = response.lastEvaluatedKey().get("eventTime").s();
            } else {
                lastKey = null;
            }
        } while (lastKey != null);
        return total;
    }

}
