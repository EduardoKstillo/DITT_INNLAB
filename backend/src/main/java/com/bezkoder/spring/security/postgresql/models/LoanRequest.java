package com.bezkoder.spring.security.postgresql.models;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;
import java.time.LocalDateTime;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@EqualsAndHashCode
@Entity
@Table(name = "loan_requests")
public class LoanRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    private Project project; // Proyecto que realiza la solicitud

    @NotNull
    private LocalDate reservationDate;

    @NotBlank
    private String timeSlot;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime createdAt = OffsetDateTime.now(); // Fecha de creación de la solicitud

    @Enumerated(EnumType.STRING)
    @NotNull
    private LoanRequestStatus status; // PENDING, APPROVED, REJECTED

    @OneToMany(mappedBy = "loanRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<LoanRequestDevice> loanRequestDevices = new HashSet<>(); // Dispositivos solicitados

    // Nuevo campo para almacenar quién aprueba o rechaza la solicitud
    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;  // Moderador que aprobó la solicitud

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime approvedAt; // Fecha de aprobación

    @ManyToOne
    @JoinColumn(name = "rejected_by")
    private User rejectedBy;  // Moderador que rechazó la solicitud

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime rejectedAt; // Fecha de rechazo

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime returnAt; // Fecha de devolución mandada por el lider del proyecto

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime approveReturnAt; // Fecha de devolución aprobada por el moderador

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime rejectedReturnAt; // Fecha de devolución aprobada/rechazada por el moderador

}