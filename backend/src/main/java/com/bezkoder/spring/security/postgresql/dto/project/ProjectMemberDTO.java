package com.bezkoder.spring.security.postgresql.dto.project;

import lombok.Data;

@Data
public class ProjectMemberDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
}