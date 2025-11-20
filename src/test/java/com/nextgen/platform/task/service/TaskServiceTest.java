package com.nextgen.platform.task.service;

import com.nextgen.platform.audit.service.TaskEventService;
import com.nextgen.platform.task.dao.TaskDAO;
import com.nextgen.platform.task.domain.Task;
import com.nextgen.platform.task.domain.TaskContainer;
import com.nextgen.platform.task.domain.TaskPriority;
import com.nextgen.platform.task.domain.TaskStatus;
import com.nextgen.platform.task.dto.TaskSearchRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskDAO taskDAO;
    private TaskEventService taskEventService;
    private TaskService taskService;

    @BeforeEach
    void setup() {
        taskDAO = mock(TaskDAO.class);
        taskEventService = mock(TaskEventService.class);
        taskService = new TaskService();
        taskService = spy(taskService);
        ReflectionTestUtils.setField(taskService, "taskDAO", taskDAO);
        ReflectionTestUtils.setField(taskService, "taskEventService", taskEventService);
    }

    private Task sampleTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Desc");
        task.setPriority(TaskPriority.HIGH);
        task.setStatus(TaskStatus.PENDING);
        task.setAssignedTo(10L);
        task.setDueDate(LocalDateTime.parse("2025-12-03T10:00:00"));
        return task;
    }

    @Test
    void createTask_nullInput_returnsNull() {
        assertNull(taskService.createTask(null));
        verify(taskDAO, never()).createTask(any());
    }

    @Test
    void createTask_validInput() {

        Task input = sampleTask();
        Task saved = sampleTask();
        when(taskDAO.createTask(input)).thenReturn(saved);
        Task result = taskService.createTask(input);
        assertEquals(saved, result);
        verify(taskEventService, times(1))
                .recordCreated(eq("10"), eq("1"), anyMap());
    }

    @Test
    void updateTask_nullInput() {
        assertNull(taskService.updateTask(null));
        verify(taskDAO, never()).updateTask(any());
    }

    @Test
    void updateTask_validInput() {

        Task input = sampleTask();
        Task updated = sampleTask();
        updated.setTitle("New Title");
        when(taskDAO.updateTask(input)).thenReturn(updated);
        Task result = taskService.updateTask(input);
        assertEquals(updated, result);
        verify(taskEventService, times(1))
                .recordUpdated(eq("10"), eq("1"), anyMap());
    }

    @Test
    void getTaskById_nullId() {
        assertNull(taskService.getTaskById(null));
        verify(taskDAO, never()).getTaskById(any());
    }

    @Test
    void getTaskById_validInput() {
        Task saved = sampleTask();
        when(taskDAO.getTaskById(1L)).thenReturn(saved);
        Task result = taskService.getTaskById(1L);
        assertEquals(saved, result);
    }

    @Test
    void searchTasks_nullParams() {
        assertNull(taskService.searchTasks(null, 10L));
        assertNull(taskService.searchTasks(new TaskSearchRequest(), null));
    }

    @Test
    void searchTasks_validInput() {
        TaskSearchRequest req = new TaskSearchRequest();
        TaskContainer container = new TaskContainer();
        when(taskDAO.searchTasks(req, 10L)).thenReturn(container);
        TaskContainer result = taskService.searchTasks(req, 10L);
        assertEquals(container, result);
        verify(taskDAO, times(1)).searchTasks(req, 10L);
    }


    @Test
    void deleteTask_noTaskFound() {
        when(taskDAO.getTaskById(1L)).thenReturn(null);
        boolean result = taskService.deleteTask(1L, 10L);
        assertFalse(result);
        verify(taskDAO, never()).deleteTask(any());
        verify(taskEventService, never()).recordDeleted(any(), any(), any());
    }

    @Test
    void deleteTask_validInput() {
        Task existing = sampleTask();
        when(taskDAO.getTaskById(1L)).thenReturn(existing);
        when(taskDAO.deleteTask(1L)).thenReturn(true);
        boolean result = taskService.deleteTask(1L, 10L);
        assertTrue(result);
        verify(taskEventService, times(1))
                .recordDeleted(eq("10"), eq("1"), anyMap());
    }
}
