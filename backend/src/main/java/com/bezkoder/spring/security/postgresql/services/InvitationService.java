package com.bezkoder.spring.security.postgresql.services;

import com.bezkoder.spring.security.postgresql.dto.NotificationRequest;
import com.bezkoder.spring.security.postgresql.exception.ResourceNotFoundException;
import com.bezkoder.spring.security.postgresql.models.Invitation;
import com.bezkoder.spring.security.postgresql.models.InvitationStatus;
import com.bezkoder.spring.security.postgresql.models.Project;
import com.bezkoder.spring.security.postgresql.models.User;
import com.bezkoder.spring.security.postgresql.repository.InvitationRepository;
import com.bezkoder.spring.security.postgresql.repository.ProjectRepository;
import com.bezkoder.spring.security.postgresql.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final UserTokenService userTokenService;
    private final FCMService fcmService;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Autowired
    public InvitationService(InvitationRepository invitationRepository,
                             UserTokenService userTokenService,
                             FCMService fcmService,
                             ProjectRepository projectRepository,
                             UserRepository userRepository) {
        this.invitationRepository = invitationRepository;
        this.userTokenService = userTokenService;
        this.fcmService = fcmService;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public void inviteMembers(Long projectId, List<String> emails, Long inviterId) {
        // Buscar proyecto y usuario que invita
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        User inviter = userRepository.findById(inviterId)
                .orElseThrow(() -> new ResourceNotFoundException("Inviter not found"));

        for (String email : emails) {
            User invitedUser = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

            Invitation invitation = new Invitation();
            invitation.setProject(project);
            invitation.setInvitedUser(invitedUser);
            invitation.setInviter(inviter);
            invitation.setStatus(InvitationStatus.PENDING);
            invitation.setSentDate(OffsetDateTime.now());

            invitationRepository.save(invitation);

            // Enviar notificación push
            sendInvitationNotification(invitedUser, project);
        }
    }

    private void sendInvitationNotification(User user, Project project) {
        String token = userTokenService.getTokenByUserId(user.getId());
        if (token != null) {
            NotificationRequest notificationRequest = new NotificationRequest();
            notificationRequest.setTitle("Invitación a un proyecto");
            notificationRequest.setBody("Has sido invitado al proyecto: " + project.getName());
            notificationRequest.setToken(token);

            try {
                fcmService.sendMessageToToken(notificationRequest);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Error al enviar la notificación", e);
            }
        }
    }

    public List<Invitation> getInvitationsForUser(Long userId) {
        return invitationRepository.findByInvitedUserIdAndStatus(userId, InvitationStatus.PENDING);
    }

    public void acceptInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        // Cambiar el estado a ACEPTADO
        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitation.setResponseDate(OffsetDateTime.now());
        invitationRepository.save(invitation);

        // Agregar miembro al proyecto
        Project project = invitation.getProject();
        User invitedUser = invitation.getInvitedUser();
        project.addMember(invitedUser);
        projectRepository.save(project);

        // Enviar notificación al líder del proyecto
        sendResponseNotificationToInviter(invitation, true);
    }

    public void rejectInvitation(Long invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResourceNotFoundException("Invitation not found"));

        // Cambiar el estado a RECHAZADO
        invitation.setStatus(InvitationStatus.REJECTED);
        invitation.setResponseDate(OffsetDateTime.now());
        invitationRepository.save(invitation);

        // Enviar notificación al líder del proyecto
        sendResponseNotificationToInviter(invitation, false);
    }

    @Transactional
    private void sendResponseNotificationToInviter(Invitation invitation, boolean accepted) {
        User inviter = invitation.getInviter();  // Usuario que envió la invitación
        String token = userTokenService.getTokenByUserId(inviter.getId());

        if (token != null) {
            String messageBody;
            if (accepted) {
                messageBody = invitation.getInvitedUser().getFirstName() + " " + invitation.getInvitedUser().getLastName() +
                        " ha aceptado tu invitación al proyecto: " + invitation.getProject().getName();
            } else {
                messageBody = invitation.getInvitedUser().getFirstName() + " " + invitation.getInvitedUser().getLastName() +
                        " ha rechazado tu invitación al proyecto: " + invitation.getProject().getName();
            }

            NotificationRequest notificationRequest = new NotificationRequest();
            notificationRequest.setTitle("Respuesta a la invitación");
            notificationRequest.setBody(messageBody);
            notificationRequest.setToken(token);

            try {
                fcmService.sendMessageToToken(notificationRequest);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Error al enviar la notificación al líder del proyecto", e);
            }
        }
    }
}
