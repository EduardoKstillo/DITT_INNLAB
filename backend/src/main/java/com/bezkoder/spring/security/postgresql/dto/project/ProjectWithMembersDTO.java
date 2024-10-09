package com.bezkoder.spring.security.postgresql.dto.project;

import lombok.Data;

import java.util.List;

@Data
public class ProjectWithMembersDTO {
    private Long id;
    private String name;
    private String leaderName;
    private String description;
    private List<ProjectMemberDTO> members;

    // Getters y Setters
}