package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.dto.InvitationRequest;
import com.bezkoder.spring.security.postgresql.models.Invitation;
import com.bezkoder.spring.security.postgresql.models.InvitationStatus;
import com.bezkoder.spring.security.postgresql.services.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invitations")
public class InvitationController {

    private final InvitationService invitationService;

    @Autowired
    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    // Enviar invitaciones a usuarios
    @PostMapping("/project/{projectId}/invite")
    public ResponseEntity<Void> inviteMembers(@RequestBody InvitationRequest invitationRequest) {
        invitationService.inviteMembers(invitationRequest.getProjectId(), invitationRequest.getEmails(), invitationRequest.getInviterId());
        return ResponseEntity.ok().build();
    }
    // Obtener todas las invitaciones pendientes de un usuario
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Invitation>> getUserInvitations(@PathVariable Long userId) {
        List<Invitation> invitations = invitationService.getInvitationsForUser(userId);
        return ResponseEntity.ok(invitations);
    }

    // Aceptar invitación
    @PostMapping("/{invitationId}/accept")
    public ResponseEntity<Void> acceptInvitation(@PathVariable Long invitationId) {
        invitationService.acceptInvitation(invitationId);
        return ResponseEntity.ok().build();
    }

    // Rechazar invitación
    @PostMapping("/{invitationId}/reject")
    public ResponseEntity<Void> rejectInvitation(@PathVariable Long invitationId) {
        invitationService.rejectInvitation(invitationId);
        return ResponseEntity.ok().build();
    }
}
