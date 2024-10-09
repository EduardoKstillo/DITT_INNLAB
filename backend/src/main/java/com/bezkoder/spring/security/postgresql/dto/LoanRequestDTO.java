package com.bezkoder.spring.security.postgresql.dto;

import com.bezkoder.spring.security.postgresql.dto.project.ProjectDTO;
import com.bezkoder.spring.security.postgresql.dto.project.ProjectSimpleDTO;
import com.bezkoder.spring.security.postgresql.models.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.bezkoder.spring.security.postgresql.models.LoanRequestStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Set;

@Data
public class LoanRequestDTO {

    private Long id;
    private ProjectSimpleDTO project;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime startDate;

    private LoanRequestStatus status;
    private String purpose;
    private Set<LoanRequestDeviceDTO> loanRequestDevices;

    private User approvedBy;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime approvedAt;

    private User rejectedBy;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime rejectedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime returnAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime approveReturnAt;

}