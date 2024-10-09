package com.bezkoder.spring.security.postgresql.dto.project;

import com.bezkoder.spring.security.postgresql.dto.user.UserResponseDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private Long leaderId; // Datos completos del líder del proyecto
    private String status;
    private List<ProjectMemberDTO> members; // Miembros del proyecto
    private List<Long> loanRequestIds; // IDs de solicitudes de préstamos
}