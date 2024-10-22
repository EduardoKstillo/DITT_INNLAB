package com.bezkoder.spring.security.postgresql.dto.laboratorydevice;

import com.bezkoder.spring.security.postgresql.models.EDevice;
import com.bezkoder.spring.security.postgresql.models.EDeviceStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LaboratoryDeviceDTO {
    private Long id;
    private EDevice type;
    private String description;
    private String characteristics;
    private String series;
    private Integer quantity;
    private String additional;
    private String sensorType;
    private String color;
    private EDeviceStatus status;
}