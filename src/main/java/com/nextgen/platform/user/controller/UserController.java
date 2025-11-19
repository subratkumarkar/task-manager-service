package com.nextgen.platform.user.controller;

import com.nextgen.platform.security.JwtService;
import com.nextgen.platform.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    /**
     * Deletes a User
     * @return ResponseEntity with no content (204) after deletion.
     */
    @DeleteMapping("/me")
    public ResponseEntity<Boolean> deleteUser(@RequestHeader("Authorization") String authHeader) {
        Long userId = jwtService.getUserIdFromAuthHeader(authHeader); // secure userId from token
        if(Objects.isNull(userId)) {
            return ResponseEntity.notFound().build();
        }
        boolean isUserDeleted = userService.deleteUser(userId);
        return isUserDeleted?ResponseEntity.noContent().build():ResponseEntity.notFound().build();
    }
}
