package com.bezkoder.spring.security.postgresql.dto.project;

import com.bezkoder.spring.security.postgresql.models.ProjectStatus;
import lombok.Data;

import java.util.List;

@Data
public class ProjectWithMembersDTO {
    private Long id;
    private String name;
    private String leaderName;
    private String description;
    private ProjectStatus status;
    private List<ProjectMemberDTO> members;

    // Getters y Setters
}