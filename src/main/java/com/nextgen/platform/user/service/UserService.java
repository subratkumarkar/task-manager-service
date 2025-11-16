package com.nextgen.platform.user.service;

import com.nextgen.platform.user.dao.UserDAO;
import com.nextgen.platform.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserDAO userDAO;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User createUser(@RequestBody User user) {
        if(Objects.isNull(user) || !StringUtils.hasText(user.getEmail()) || !StringUtils.hasText(user.getPassword())) {
            return null;
        }
        return userDAO.createUser(user);
    }

    public User getUserById(Long id) {
        if(Objects.isNull(id)){
            return null;
        }
        return  userDAO.getUserById(id);
    }

    public boolean deleteUser(Long id) {
        log.debug("Delete task with id {}", id);
        return userDAO.deleteUser(id);
    }

    public boolean authenticate(User user) {
        if(Objects.isNull(user) || !StringUtils.hasText(user.getEmail()) || !StringUtils.hasText(user.getPassword())) {
             return false;
        }
        Pair<String, String> userInfo = userDAO.getUserEmailAndPasswordHash(user.getEmail());
        if(Objects.isNull(userInfo)) {
            return false;
        }
        if(passwordEncoder.matches(user.getPassword(), userInfo.getSecond())){
            userDAO.updateLastLoginTime(user.getEmail());
            return true;
        }
        return false;
    }

}
