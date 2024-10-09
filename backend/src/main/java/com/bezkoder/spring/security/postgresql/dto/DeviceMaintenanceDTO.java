package com.bezkoder.spring.security.postgresql.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceMaintenanceDTO {
    private Long id;
    private Long deviceId;
    private LocalDateTime maintenanceDate;
    private String details;
    private LocalDateTime expectedReturnDate;
}