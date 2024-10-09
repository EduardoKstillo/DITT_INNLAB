package com.bezkoder.spring.security.postgresql.dto;

import lombok.Data;

@Data
public class DeviceImageDTO {
    private Long id;
    private String imagePath;
    private Long deviceId;
}