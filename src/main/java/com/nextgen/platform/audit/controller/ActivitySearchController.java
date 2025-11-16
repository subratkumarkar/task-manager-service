package com.nextgen.platform.audit.controller;

import com.nextgen.platform.audit.model.UserActivity;
import com.nextgen.platform.audit.service.ActivitySearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users/{userId}/activities")
@RequiredArgsConstructor
@Slf4j
public class ActivitySearchController {

    @Autowired
    private final ActivitySearchService activitySearchService;

    /**
     * Get all events for a specific user.
     *
     * @param userId The user whose events are to be fetched.
     * @param limit Maximum number of records to return (optional).
     * @param lastKey Pagination last evaluated key (optional).
     * @return List of UserActivity objects.
     */
    @GetMapping
    public ResponseEntity<List<UserActivity>> getUserEvents(
            @PathVariable String userId,
            @RequestParam(required = false, defaultValue = "20") int limit,
            @RequestParam(required = false) String lastKey) {
        log.info("Fetching events for user {} with limit {}, lastKey {}", userId, limit, lastKey);
        List<UserActivity> events = activitySearchService.getUserEvents(userId, limit, lastKey);
        return ResponseEntity.ok(events);
    }
}

