package com.bezkoder.spring.security.postgresql.dto;

import lombok.Data;

@Data
public class LoanRequestDeviceDTO {

    private Long id;
    private Long loanRequestId; // ID de la solicitud de préstamo
    private Long deviceId;      // ID del dispositivo
    private int quantity;       // Cantidad del dispositivo
    private String description;

}