package com.bezkoder.spring.security.postgresql.dto.project;

import com.bezkoder.spring.security.postgresql.dto.user.LeaderDTO;
import lombok.Data;

@Data
public class ProjectSimpleDTO {
    private Long id;
    private String name;
    private String description;
    private LeaderDTO leader;
}
