package com.bezkoder.spring.security.postgresql.payload.request;

import java.util.Date;
import java.util.Set;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class SignupRequest {
  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  private Set<String> role;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;

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

  private Date birthDate;

  @Size(max = 255)
  private String photo;

}
