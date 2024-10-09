package com.bezkoder.spring.security.postgresql.dto;

import lombok.Data;

@Data
public class TokenDTO {
    private Long userId;
    private String token;
}