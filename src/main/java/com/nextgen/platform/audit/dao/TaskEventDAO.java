package com.nextgen.platform.audit.dao;

import com.nextgen.platform.audit.model.TaskEvent;

public interface TaskEventDAO {
    void save(TaskEvent event);
}
