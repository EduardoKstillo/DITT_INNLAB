package com.bezkoder.spring.security.postgresql.dto.user;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

// Para devolver datos del usuario en las respuestas HTTP (en GET).
@Data
public class UserResponseDTO {

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String university;
    private String phone;
    private String dni;
    private LocalDate birthDate;
}