package com.nextgen.platform.audit.controller;

import com.nextgen.platform.audit.dto.ActivitySearchResponse;
import com.nextgen.platform.audit.model.UserActivity;
import com.nextgen.platform.audit.service.ActivitySearchService;
import com.nextgen.platform.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users/activities")
@RequiredArgsConstructor
@Slf4j
public class ActivitySearchController {

    @Autowired
    private final ActivitySearchService activitySearchService;
    @Autowired
    private JwtService jwtService;
    /**
     * Get all events for a specific user.
     *
     * @param authHeader contains the user whose events are to be fetched.
     * @param limit Maximum number of records to return (optional).
     * @param lastKey Pagination last evaluated key (optional).
     * @return List of ActivitySearchResponse objects.
     */
    @GetMapping
    public ResponseEntity<ActivitySearchResponse> getUserEvents(
            @RequestParam(required = false, defaultValue = "20") int limit,
            @RequestParam(required = false) String lastKey,
            @RequestHeader("Authorization") String authHeader) {
        Long userId = jwtService.getUserIdFromAuthHeader(authHeader);
        if(Objects.isNull(userId)) {
            return ResponseEntity.badRequest().build();
        }
       ActivitySearchResponse activitySearchResponse= activitySearchService.getUserEvents(userId.toString(), limit, lastKey);
        return ResponseEntity.ok(activitySearchResponse);
    }
}

