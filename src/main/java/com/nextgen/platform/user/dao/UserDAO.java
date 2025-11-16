package com.nextgen.platform.user.dao;

import com.nextgen.platform.user.domain.User;
import org.springframework.data.util.Pair;

import java.util.Map;


public interface UserDAO {
    User getUserById(Long id);
    User createUser(User user);
    boolean deleteUser(Long id);
    Pair<String, String> getUserEmailAndPasswordHash(String email);
    boolean updateLastLoginTime(String email);
}
