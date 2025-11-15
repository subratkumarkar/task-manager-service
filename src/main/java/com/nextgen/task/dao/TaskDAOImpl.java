package com.nextgen.task.dao;

import com.nextgen.task.domain.Task;
import com.nextgen.task.domain.TaskPriority;
import com.nextgen.task.domain.TaskStatus;
import com.nextgen.task.dto.TaskSearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Repository
public class TaskDAOImpl implements TaskDAO {
    private static String DELETE_SQL = "UPDATE user_tasks set deleted=:deleted WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    public TaskDAOImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Task getTaskById(Long id) {
        log.debug("Getting task by id {}", id);
        if (id == null) {
            return null;
        }
        String sql = "SELECT id, title, description, status, priority, due_date, assigned_to, created_at, updated_at "
                + "FROM user_tasks WHERE id = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        try {
            return jdbcTemplate.queryForObject(sql, params, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Task> getTasks(TaskSearchRequest request) {
        log.debug("Getting task by TaskSearchRequest {}", request);
        if(!StringUtils.hasText(request.getAssignedTo())) {
            return Collections.emptyList();
        }
        StringBuilder sql = new StringBuilder("SELECT id, title, description, status, priority, due_date, ")
                .append("assigned_to, updated_at FROM user_tasks WHERE assigned_to=:assignedTo");

        Map<String, Object> sqlParams = new HashMap<>();
        sqlParams.put("assignedTo", request.getAssignedTo());
        //build search query
        QueryUtil.buildSearchQuery(request, sql, sqlParams);
        //build sort order
        QueryUtil.buildSortOrder(request, sql);
        // Pagination
        sql.append(" LIMIT :limit OFFSET :offset");
        sqlParams.put("limit", request.getLimit());
        sqlParams.put("offset", request.getStartIndex());
        return jdbcTemplate.query(sql.toString(), sqlParams, rowMapper);
    }



    @Override
    public Task createTask(Task task) {
        if (task == null || !StringUtils.hasText(task.getAssignedTo())) {
            return null;
        }
        String sql = "INSERT INTO user_tasks (title, description, status, priority, due_date, assigned_to) "
                + "VALUES (:title, :description, :status, :priority, :dueDate, :assignedTo)";

        Map<String, Object> params = new HashMap<>();
        params.put("title", task.getTitle());
        params.put("description", task.getDescription());
        params.put("status", task.getStatus() != null ? task.getStatus().name() : null);
        params.put("priority", task.getPriority() != null ? task.getPriority().name() : null);
        params.put("dueDate", task.getDueDate() != null ? Timestamp.valueOf(task.getDueDate()) : null);
        params.put("assignedTo", task.getAssignedTo());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder);
        if (keyHolder.getKey() != null) {
            task.setId(keyHolder.getKey().longValue());
        }
        return task;
    }

    @Override
    public Task updateTask(Task task) {
        if (task == null || task.getId() == null) {
            return null;   // cannot update without ID
        }
        String sql = "UPDATE user_tasks SET title = :title, description = :description, status = :status, "
                + "priority = :priority, due_date = :dueDate WHERE id = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("title", task.getTitle());
        params.put("description", task.getDescription());
        params.put("status", task.getStatus() != null ? task.getStatus().name() : null);
        params.put("priority", task.getPriority() != null ? task.getPriority().name() : null);
        params.put("dueDate", task.getDueDate() != null ? Timestamp.valueOf(task.getDueDate()) : null);
        params.put("id", task.getId());
        int updated = jdbcTemplate.update(sql, params);
        return updated > 0 ? task : null;
    }

    @Override
    public boolean deleteTask(Long id) {
        if (id == null) {
            return false;
        }
        //soft delete
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("deleted", true);
        int updatedRows = jdbcTemplate.update(DELETE_SQL, params);
        return updatedRows>0;
    }

    @Override
    public boolean activateTask(Long id) {
        if (id == null) {
            return false;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("deleted", false);
        int updatedRows = jdbcTemplate.update(DELETE_SQL, params);
        return updatedRows>0;
    }

    private final RowMapper<Task> rowMapper = (rs, rowNum) -> {
        Task task = new Task();
        task.setId(rs.getLong("id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        String statusStr = rs.getString("status");
        task.setStatus(statusStr != null ? TaskStatus.valueOf(statusStr) : null);
        String priorityStr = rs.getString("priority");
        task.setPriority(priorityStr != null ? TaskPriority.valueOf(priorityStr) : null);
        Timestamp due = rs.getTimestamp("due_date");
        task.setDueDate(due != null ? due.toLocalDateTime() : null);
        task.setAssignedTo(rs.getString("assigned_to"));
        Timestamp updatedAtTs = rs.getTimestamp("updated_at");
        task.setUpdatedAt(updatedAtTs != null ? updatedAtTs.toLocalDateTime() : null);
        return task;
    };
}
