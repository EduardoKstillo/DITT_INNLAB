package com.bezkoder.spring.security.postgresql.dto.user;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

// Para recibir datos del usuario desde las solicitudes HTTP (normalmente en POST y PUT).
@Data
public class UserRequestDTO {

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(max = 50)
    private String university;

    @Size(max = 20)
    private String phone;

    @Size(max = 20)
    private String dni;

    private LocalDate birthDate;
}