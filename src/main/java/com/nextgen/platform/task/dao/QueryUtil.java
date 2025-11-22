package com.nextgen.platform.task.dao;

import com.nextgen.platform.task.dto.SortByField;
import com.nextgen.platform.task.dto.TaskSearchRequest;
import org.springframework.util.StringUtils;


import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;

public class QueryUtil {

    public static void buildSearchQuery(TaskSearchRequest request, StringBuilder sql, Map<String, Object> sqlParams) {
        if(Objects.isNull(request) || Objects.isNull(sql) || Objects.isNull(sqlParams)) {
            return;
        }
        if (request.getStatus() != null) {
            sql.append(" AND status = :status");
            sqlParams.put("status", request.getStatus().name());
        }
        if (request.getPriority() != null) {
            sql.append(" AND priority = :priority");
            sqlParams.put("priority", request.getPriority().name());
        }
        if (StringUtils.hasText(request.getTitle())) {
            sql.append(" AND (title LIKE :title)");
            sqlParams.put("title", "%" + request.getTitle() + "%");
        }
        if (StringUtils.hasText(request.getDescription())) {
            sql.append(" AND (description LIKE :description)");
            sqlParams.put("description", "%" + request.getDescription() + "%");
        }
        if (request.getFromDueDate() != null) {
            sql.append(" AND due_date >= :fromDueDate");
            sqlParams.put("fromDueDate", Timestamp.valueOf(request.getFromDueDate()));
        }
        if (request.getToDueDate() != null) {
            sql.append(" AND due_date <= :toDueDate");
            sqlParams.put("toDueDate", Timestamp.valueOf(request.getToDueDate()));
        }
        if (request.getFromUpdatedAt() != null) {
            sql.append(" AND updated_at >= :modifiedFrom");
            sqlParams.put("modifiedFrom", Timestamp.valueOf(request.getFromUpdatedAt()));
        }
        if (request.getToUpdatedAt() != null) {
            sql.append(" AND updated_at <= :modifiedTo");
            sqlParams.put("modifiedTo", Timestamp.valueOf(request.getToUpdatedAt()));
        }
    }

    public static void buildSortOrder(TaskSearchRequest request, StringBuilder sql) {
        if(Objects.nonNull(request) && Objects.nonNull(request.getSortBy())
                && Objects.nonNull(request.getSortDirection())
                && Objects.nonNull(sql)) {
            String sortColumn = SortByField.getDBFieldName(request.getSortBy());
            if(StringUtils.hasText(sortColumn)) {
                String sortDirection = request.getSortDirection().name();
                if (SortByField.priority.equals(request.getSortBy())) {
                    sql.append(" ORDER BY CASE priority ")
                            .append("WHEN 'LOW' THEN 1 ")
                            .append("WHEN 'MEDIUM' THEN 2 ")
                            .append("WHEN 'HIGH' THEN 3 ")
                            .append("END ").append(sortDirection);
                } else if (SortByField.status.equals(request.getSortBy())) {
                    sql.append(" ORDER BY CASE status ")
                            .append("WHEN 'COMPLETE' THEN 1 ")
                            .append("WHEN 'IN_PROGRESS' THEN 2 ")
                            .append("WHEN 'PENDING' THEN 3 ")
                            .append("END ").append(sortDirection);
                }else {
                    // default ordering for other columns
                    sql.append(" ORDER BY ").append(sortColumn).append(" ").append(sortDirection);
                }
            }
        }
    }
}
