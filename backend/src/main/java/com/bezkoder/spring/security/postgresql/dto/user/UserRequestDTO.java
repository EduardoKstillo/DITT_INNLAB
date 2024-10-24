package com.bezkoder.spring.security.postgresql.dto.user;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

// Para recibir datos del usuario desde las solicitudes HTTP (normalmente en POST y PUT).
@Data
public class UserRequestDTO {

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> roles; // Este campo puede ser opcional en actualizaciones

    @Size(min = 6, max = 40)
    private String password; // Este campo debe ser opcional en actualizaciones

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(max = 50)
    private String university;

    @Size(max = 20)
    private String phone;

    @Size(max = 8)
    private String dni;

    @Size(max = 255)
    private String photo;
}