package com.bezkoder.spring.security.postgresql.dto;

import lombok.Data;

import java.util.List;

@Data
public class InvitationRequest {
    private Long projectId;
    private List<String> emails; // Lista de correos electrónicos de los usuarios invitados
    private Long inviterId; // ID del usuario que envía la invitación
}