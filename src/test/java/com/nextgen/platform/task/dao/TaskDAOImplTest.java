package com.nextgen.platform.task.dao;

import com.nextgen.platform.task.domain.Task;
import com.nextgen.platform.task.domain.TaskContainer;
import com.nextgen.platform.task.domain.TaskPriority;
import com.nextgen.platform.task.domain.TaskStatus;
import com.nextgen.platform.task.dto.SortByField;
import com.nextgen.platform.task.dto.SortOrder;
import com.nextgen.platform.task.dto.TaskSearchRequest;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(TaskDAOImpl.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskDAOImplTest {

    List<Long> taskIds = new ArrayList<>();
    @Autowired
    private TaskDAOImpl dao;

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @BeforeAll
    void setup() {
        jdbc.getJdbcTemplate().execute("DELETE FROM user_tasks");
        createTask();
    }

    private void createTask() {
        Task t = new Task();
        t.setTitle("Task A");
        t.setDescription("New Desc");
        t.setStatus(TaskStatus.PENDING);
        t.setPriority(TaskPriority.HIGH);
        t.setAssignedTo(1L);
        t.setDueDate(LocalDateTime.now().plusDays(1));
        Task saved = dao.createTask(t);
        assertThat(saved.getId()).isNotNull();

        Task dbTask = dao.getTaskById(saved.getId());
        assertThat(dbTask.getTitle()).isEqualTo("Task A");
        assertThat(dbTask.getPriority()).isEqualTo(TaskPriority.HIGH);
        taskIds.add(dbTask.getId());

        t = new Task();
        t.setTitle("Task B");
        t.setDescription("New Desc");
        t.setStatus(TaskStatus.COMPLETE);
        t.setPriority(TaskPriority.HIGH);
        t.setAssignedTo(1L);
        t.setDueDate(LocalDateTime.now().plusDays(1));
        saved = dao.createTask(t);
        taskIds.add(saved.getId());
    }

    @Test
    void testGetTaskById() {
        Task t = dao.getTaskById(taskIds.get(0));
        assertThat(t).isNotNull();
        assertThat(t.getId()).isEqualTo(taskIds.get(0));
    }

    @Test
    void testSearchTasksByTitle() {
        TaskSearchRequest req = new TaskSearchRequest();
        req.setTitle("Task");
        req.setStartIndex(0);
        req.setLimit(10);

        TaskContainer result = dao.searchTasks(req, 1L);
        assertThat(result.getItems().size()).isEqualTo(2);
    }

    @Test
    void testSearchTasksByStatus() {
        TaskSearchRequest req = new TaskSearchRequest();
        req.setStatus(TaskStatus.COMPLETE);
        req.setStartIndex(0);
        req.setLimit(10);

        TaskContainer result = dao.searchTasks(req, 1L);

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getTitle()).isEqualTo("Task B");
    }

    @Test
    void testSearchTasksWithPagination() {
        TaskSearchRequest req = new TaskSearchRequest();
        req.setStartIndex(0);
        req.setLimit(1); // only 1 per page

        TaskContainer result = dao.searchTasks(req, 1L);

        assertThat(result.getItems().size()).isEqualTo(1);
        assertThat(result.getTotalCount()).isEqualTo(2);
    }

    @Test
    void testSearchTasksWithSort() {
        TaskSearchRequest req = new TaskSearchRequest();
        req.setSortBy(SortByField.title);
        req.setSortDirection(SortOrder.DESC);
        req.setStartIndex(0);
        req.setLimit(10);

        TaskContainer result = dao.searchTasks(req, 1L);
        assertThat(result.getItems().get(0).getTitle()).isEqualTo("Task B");
    }


    @Test
    void testUpdateTask() {
        Task t = dao.getTaskById(taskIds.get(0));
        t.setTitle("Updated Title");
        Task updated = dao.updateTask(t);
        assertThat(updated).isNotNull();
        assertThat(updated.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    void testDeleteTask() {
        boolean deleted = dao.deleteTask(taskIds.get(0));
        assertThat(deleted).isTrue();
        Task t = dao.getTaskById(taskIds.get(0));
        assertThat(t).isNull();
    }

}

