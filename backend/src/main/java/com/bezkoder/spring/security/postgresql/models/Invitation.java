package com.bezkoder.spring.security.postgresql.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "invitations")
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false) // Aseguramos que siempre haya un proyecto asociado
    private Project project;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "invited_user_id", nullable = false) // El usuario invitado no puede ser nulo
    private User invitedUser;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "inviter_id", nullable = false) // El invitador tampoco puede ser nulo
    private User inviter; // El usuario que envía la invitación (el líder del proyecto)

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvitationStatus status;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime sentDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime responseDate;
}