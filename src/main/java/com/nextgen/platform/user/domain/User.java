package com.nextgen.platform.user.domain;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
}
