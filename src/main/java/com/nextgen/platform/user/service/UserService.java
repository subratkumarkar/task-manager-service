package com.nextgen.platform.user.service;

import com.nextgen.platform.user.dao.UserDAO;
import com.nextgen.platform.user.domain.User;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Objects;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserDAO userDAO;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User createUser(@NonNull @RequestBody User user) {
        return userDAO.createUser(user);
    }

    public boolean deleteUser(Long id) {
        log.debug("Delete task with id {}", id);
        return userDAO.deleteUser(id);
    }

    public User authenticate(User user) {
        if(Objects.isNull(user) || !StringUtils.hasText(user.getEmail()) || !StringUtils.hasText(user.getPassword())) {
             return null;
        }
        User userInfo = userDAO.getUserAuthInfo(user.getEmail());
        if(Objects.isNull(userInfo) || StringUtils.isEmpty(userInfo.getPassword())
           || StringUtils.isEmpty(userInfo.getEmail())) {
            return null;
        }
        if(passwordEncoder.matches(user.getPassword(), userInfo.getPassword())){
            userDAO.updateLastLoginTime(user.getEmail());
            return userInfo;
        }
        return null;
    }

}
