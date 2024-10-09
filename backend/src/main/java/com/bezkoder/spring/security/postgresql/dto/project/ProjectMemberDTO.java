package com.bezkoder.spring.security.postgresql.dto.project;

import lombok.Data;

@Data
public class ProjectMemberDTO {
    private String email;
    private String firstName;
    private String lastName;
}