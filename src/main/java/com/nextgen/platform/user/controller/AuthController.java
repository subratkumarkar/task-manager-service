package com.nextgen.platform.user.controller;

import com.nextgen.platform.security.JwtService;
import com.nextgen.platform.user.domain.User;
import com.nextgen.platform.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    /**
     * Authenticate a User by email and password.
     * @param request the user to be authenticated.
     * @return ResponseEntity with token and userId
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {
        log.info("login request received");
        User user = userService.authenticate(request);
        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        String token = jwtService.generateToken(user.getId());
        return ResponseEntity.ok(Map.of("token", token, "userId", user.getId()));
    }
    /**
     * Creates a new user.
     * @param user User object containing the details to create a user profile.
     * @return ResponseEntity containing the user info
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {
        log.info("Signup request received");
        if(Objects.isNull(user) || !StringUtils.hasText(user.getEmail()) || !StringUtils.hasText(user.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Bad Input"));
        }
        User created =  userService.createUser(user);
        if(created == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "User already exists"));
        }
        return ResponseEntity.ok(created);
    }

}

