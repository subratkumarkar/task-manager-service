package com.nextgen.platform.user.dao;

import com.nextgen.platform.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.util.Pair;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class UserDAOImpl implements UserDAO {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public User createUser(User user) {
        if (user == null || !StringUtils.hasText(user.getEmail())) {
            return null;
        }
        String sql = "INSERT INTO users (first_name, last_name, email, password_hash) "
                + "VALUES (:firstName, :lastName, :email, :passwordHash)";

        Map<String, Object> params = new HashMap<>();
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("email", user.getEmail());
        params.put("passwordHash",passwordEncoder.encode(user.getPassword()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(sql, new MapSqlParameterSource(params), keyHolder);
        }catch (DuplicateKeyException ex){
            return null;
        }
        if (keyHolder.getKey() != null) {
            user.setId(keyHolder.getKey().longValue());
        }
        return user;
    }

    @Override
    public User getUserById(Long id) {
        log.debug("Getting user by id {}", id);
        if (id == null) {
            return null;
        }
        String sql = "SELECT id, first_name, last_name, email, last_login_at FROM users WHERE id = :id";

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        try {
            return jdbcTemplate.queryForObject(sql, params, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean deleteUser(Long id) {
        if (id == null) {
            return false;
        }
        //soft delete
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("deleted", true);
        int updatedRows = jdbcTemplate.update("DELETE from users where id=:id", params);
        return updatedRows>0;
    }

    @Override
    public User getUserAuthInfo(String email) {
        if (!StringUtils.hasText(email)) {
            return null;
        }
        String sql = "SELECT id, email, password_hash FROM users WHERE email = :email";

        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        try {
           return jdbcTemplate.queryForObject(sql, params, userInfoMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean updateLastLoginTime(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        //soft delete
        String sql = "UPDATE users set last_login_at=:lastLoginAt WHERE email = :email";
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);
        params.put("lastLoginAt", Timestamp.valueOf(LocalDateTime.now()));
        int updated = jdbcTemplate.update(sql, params);
        return updated>0;
    }

    private final RowMapper<User> rowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName( rs.getString("last_name"));
        user.setEmail(rs.getString("email"));
        Timestamp lastLoginAt = rs.getTimestamp("last_login_at");
        user.setLastLoginAt(lastLoginAt != null ? lastLoginAt.toLocalDateTime() : null);
        return user;
    };

    private final RowMapper<User> userInfoMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password_hash"));
        return user;
    };
}
