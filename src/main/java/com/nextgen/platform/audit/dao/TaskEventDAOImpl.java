package com.nextgen.platform.audit.dao;

import com.nextgen.platform.audit.model.TaskEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
@RequiredArgsConstructor
public class TaskEventDAOImpl implements TaskEventDAO {

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    public void save(TaskEvent event) {
        DynamoDbTable<TaskEvent> table =
                enhancedClient.table("task_events", TableSchema.fromBean(TaskEvent.class));
        table.putItem(event);
    }

}
