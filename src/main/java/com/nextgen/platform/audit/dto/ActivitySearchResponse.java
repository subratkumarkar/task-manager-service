package com.nextgen.platform.audit.dto;

import com.nextgen.platform.audit.model.UserActivity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ActivitySearchResponse {
    private List<UserActivity> items;
    private long totalCount;
}
