package com.nextgen.platform.audit.service;

import com.nextgen.platform.audit.dao.TaskEventDAO;
import com.nextgen.platform.audit.model.TaskEvent;
import com.nextgen.platform.audit.model.UserActivity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivitySearchService {

    @Autowired
    private TaskEventDAO taskEventDAO;

    public List<UserActivity> getUserEvents(String userId, int limit, String lastKey) {
        List<TaskEvent> taskEvents = taskEventDAO.findEventsByUser(userId, limit, lastKey);
        if(CollectionUtils.isEmpty(taskEvents)) {
            return Collections.emptyList();
        }
       return taskEvents.stream().map(taskEvent -> {
            UserActivity userEvent = new UserActivity();
            userEvent.setActivityTime(taskEvent.getEventTime());
            userEvent.setEventType(taskEvent.getEventType());
            if(!CollectionUtils.isEmpty(taskEvent.getDetails())){
                userEvent.setTitle(taskEvent.getDetails().get("title"));
                userEvent.setDescription(taskEvent.getDetails().get("description"));
                userEvent.setPriority(taskEvent.getDetails().get("priority"));
                userEvent.setStatus(taskEvent.getDetails().get("status"));
                userEvent.setDueDate(taskEvent.getDetails().get("dueDate"));
            }
            return userEvent;
        }).collect(Collectors.toList());
    }
}

