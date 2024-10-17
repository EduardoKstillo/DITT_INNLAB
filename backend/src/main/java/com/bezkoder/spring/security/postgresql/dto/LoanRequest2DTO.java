package com.bezkoder.spring.security.postgresql.dto;

import com.bezkoder.spring.security.postgresql.dto.project.ProjectSimpleDTO;
import com.bezkoder.spring.security.postgresql.models.LoanRequestStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Set;

@Data
public class LoanRequest2DTO {
    private Long id;
    private Long projectId;

    private LocalDate reservationDate;
    private String timeSlot;

    private Set<LoanRequestDeviceDTO> loanRequestDevices;
}
