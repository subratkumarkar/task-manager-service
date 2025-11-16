package com.nextgen.platform.user.controller;

import com.nextgen.platform.user.domain.User;
import com.nextgen.platform.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Creates a new user.
     * @param user User object containing the details to create a user profile.
     * @return ResponseEntity containing the created User, or 400 Bad Request if creation fails.
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        log.debug("Creating user {}", user);
        User created = userService.createUser(user);
        if (created == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(created);
    }

    /**
     * Retrieves a User by ID.
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity containing the User
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("Getting user with id {}", id);
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }


    /**
     * Deletes a User by its ID.
     * @param id The ID of the user to delete.
     * @return ResponseEntity with no content (204) after deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        log.debug("Delete user with id {}", id);
        boolean isUserDeleted = userService.deleteUser(id);
        return isUserDeleted?ResponseEntity.noContent().build():ResponseEntity.notFound().build();
    }

    /**
     * Authenticate a User by email and password.
     * @param user the user to be authenticated.
     * @return ResponseEntity with authentication indicator
     */
    @PostMapping("/authenticate")
    public ResponseEntity<Boolean> authenticate(@RequestBody User user) {
        return ResponseEntity.ok( userService.authenticate(user));
    }

}
