package com.nextgen.task.dto;
import java.util.Objects;

public enum SortByField {
    title,
    description,
    status,
    priority,
    dueDate,
    updatedAt;

    public static String getDBFieldName(SortByField sortByField) {
        if(Objects.isNull(sortByField)){
            return null;
        }
        if(dueDate.equals(sortByField)){
            return "due_date";
        }
        if(updatedAt.equals(sortByField)){
            return "updated_at";
        }
        return sortByField.name();
    }
}
