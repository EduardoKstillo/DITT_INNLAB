package com.bezkoder.spring.security.postgresql.services;

import com.bezkoder.spring.security.postgresql.dto.LoanRequest2DTO;
import com.bezkoder.spring.security.postgresql.dto.LoanRequestDTO;
import com.bezkoder.spring.security.postgresql.dto.LoanRequestDeviceDTO;
import com.bezkoder.spring.security.postgresql.dto.NotificationRequest;
import com.bezkoder.spring.security.postgresql.dto.project.ProjectDTO;
import com.bezkoder.spring.security.postgresql.dto.project.ProjectSimpleDTO;
import com.bezkoder.spring.security.postgresql.dto.user.LeaderDTO;
import com.bezkoder.spring.security.postgresql.dto.user.UserResponseDTO;
import com.bezkoder.spring.security.postgresql.exception.FCMExecutionException;
import com.bezkoder.spring.security.postgresql.exception.FCMInterruptedException;
import com.bezkoder.spring.security.postgresql.exception.ResourceNotFoundException;
import com.bezkoder.spring.security.postgresql.models.*;
import com.bezkoder.spring.security.postgresql.repository.*;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.management.Notification;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class LoanRequestService {

    private final LoanRequestRepository loanRequestRepository;
    private final LoanRequestDeviceRepository loanRequestDeviceRepository;
    private final LaboratoryDeviceRepository laboratoryDeviceRepository;
    private final ProjectRepository projectRepository;
    private final FCMService fcmService;
    private final UserTokenService userTokenService;

    private final UserRepository userRepository;

    @Autowired
    public LoanRequestService(LoanRequestRepository loanRequestRepository,
                              LoanRequestDeviceRepository loanRequestDeviceRepository,
                              LaboratoryDeviceRepository laboratoryDeviceRepository,
                              ProjectRepository projectRepository,
                              FCMService fcmService,
                              UserTokenService userTokenService,
                              UserRepository userRepository) {
        this.loanRequestRepository = loanRequestRepository;
        this.loanRequestDeviceRepository = loanRequestDeviceRepository;
        this.laboratoryDeviceRepository = laboratoryDeviceRepository;
        this.projectRepository = projectRepository;
        this.fcmService = fcmService;
        this.userTokenService = userTokenService;
        this.userRepository = userRepository;
    }

    public LoanRequestDTO createLoanRequest(LoanRequest2DTO loanRequestDTO) {

        // Buscar el proyecto asociado
        Project project = projectRepository.findById(loanRequestDTO.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        // Validar si ya existe una solicitud con la misma fecha, horario y estado "APPROVED" de un proyecto diferente
        boolean existsConflict = loanRequestRepository.existsByReservationDateAndTimeSlotAndStatusAndProjectIdNot(
                loanRequestDTO.getReservationDate(), loanRequestDTO.getTimeSlot(), LoanRequestStatus.APPROVED, project.getId());

        // Si existe una solicitud aprobada para otro proyecto, lanzar excepción
        if (existsConflict) {
            throw new IllegalArgumentException("Ya existe una solicitud aprobada para esta fecha y horario.");
        }

        // Crear la solicitud de préstamo
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setProject(project);
        loanRequest.setStatus(LoanRequestStatus.PENDING);
        loanRequest.setReservationDate(loanRequestDTO.getReservationDate());  // Solo la fecha
        loanRequest.setTimeSlot(loanRequestDTO.getTimeSlot());

        // Procesar y asociar dispositivos
        Set<LoanRequestDevice> loanRequestDevices = processLoanRequestDevices(loanRequestDTO, loanRequest);
        loanRequest.setLoanRequestDevices(loanRequestDevices);

        // Guardar la solicitud
        loanRequest = loanRequestRepository.save(loanRequest);

        // Notificar a los moderadores
        try {
            notifyModerators(loanRequest, "Nueva Solicitud de Préstamo", "Se ha creado una nueva solicitud de préstamo.");
        } catch (Exception e) {
            // Manejar la excepción de manera apropiada (registrar el error, etc.)
            System.err.println("Error al notificar a los moderadores: " + e.getMessage());
        }

        return mapToDTO(loanRequest);
    }

    // Procesar los dispositivos solicitados.
    private Set<LoanRequestDevice> processLoanRequestDevices(LoanRequest2DTO loanRequestDTO, LoanRequest loanRequest) {
        Set<LoanRequestDevice> loanRequestDevices = new HashSet<>();

        for (LoanRequestDeviceDTO deviceDTO : loanRequestDTO.getLoanRequestDevices()) {
            LaboratoryDevice device = laboratoryDeviceRepository.findById(deviceDTO.getDeviceId())
                    .orElseThrow(() -> new ResourceNotFoundException("Device not found"));

            validateDeviceQuantity(deviceDTO.getQuantity(), device.getQuantity());

            LoanRequestDevice loanRequestDevice = new LoanRequestDevice();
            loanRequestDevice.setQuantity(deviceDTO.getQuantity());
            loanRequestDevice.setDevice(device);
            loanRequestDevice.setLoanRequest(loanRequest);
            loanRequestDevices.add(loanRequestDevice);
        }

        return loanRequestDevices;
    }

    // Validar la cantidad de dispositivos solicitados.
    private void validateDeviceQuantity(int requestedQuantity, int availableQuantity) {
        if (requestedQuantity > availableQuantity) {
            throw new IllegalArgumentException("La cantidad solicitada excede el stock disponible.");
        }
    }
    private void notifyModerators(LoanRequest loanRequest, String title, String body) {
        List<String> moderatorTokens = userTokenService.findAllModeratorsTokens();

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setTitle(title);
        notificationRequest.setBody(body);

        try {
            fcmService.sendMessageToTokens(notificationRequest, moderatorTokens, loanRequest);
        } catch (InterruptedException | ExecutionException | FirebaseMessagingException e) {
            throw new RuntimeException("Error al enviar notificaciones", e);
        }
    }

    private void notifyLeader(LoanRequest loanRequest, String title, String body) {
        String leaderToken = getTokenForLeader(loanRequest.getProject().getLeader().getId());

        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setTitle(title);
        notificationRequest.setBody(body);
        notificationRequest.setToken(leaderToken);

        try {
            fcmService.sendMessageToToken(notificationRequest);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error al enviar la notificación", e);
        }
    }

    private String getTokenForLeader(Long leaderId) {
        // Recupera el token del líder del proyecto usando el ID del líder
        return userTokenService.getTokenByUserId(leaderId);
    }

    public List<LoanRequestDTO> getLoanRequestsByStatus(String status) {
        List<LoanRequest> loanRequests = loanRequestRepository.findByStatus(status);
        return loanRequests.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public LoanRequestDTO getLoanRequestById(Long id) {
        LoanRequest loanRequest = loanRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan request not found"));
        return mapToDTO(loanRequest);
    }

    public List<LoanRequestDTO> getAllLoanRequests() {
        return loanRequestRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private void validateStockBeforeApproval(LoanRequest loanRequest) {
        // Validar stock antes de aprobar
        for (LoanRequestDevice loanRequestDevice : loanRequest.getLoanRequestDevices()) {
            LaboratoryDevice device = laboratoryDeviceRepository.findById(loanRequestDevice.getDevice().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Dispositivo no encontrado"));
            // Verificar si hay suficiente stock
            if (loanRequestDevice.getQuantity() > device.getQuantity()) {
                throw new IllegalArgumentException("No hay suficiente stock para el dispositivo " + device.getDescription());
            }
        }
    }
    
    public void approveLoanRequest(Long id, Long moderatorId) {
        LoanRequest loanRequest = loanRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud de préstamo no encontrada"));

        if (loanRequest.getStatus() != LoanRequestStatus.PENDING) {
            throw new IllegalStateException("Esta solicitud de préstamo ya ha sido procesada y no puede aprobarse nuevamente.");
        }

        User moderator = userRepository.findById(moderatorId)
                .orElseThrow(() -> new RuntimeException("Moderator not found"));

        validateStockBeforeApproval(loanRequest);
        loanRequest.setStatus(LoanRequestStatus.APPROVED);
        loanRequest.setApprovedBy(moderator);
        loanRequest.setApprovedAt(OffsetDateTime.now());

        // Actualizar el stock de los dispositivos y cambiar el estado si es necesario
        updateDeviceStocks(loanRequest);

        loanRequestRepository.save(loanRequest);

        try {
            notifyLeader(loanRequest, "Solicitud Aprobada", "La solicitud de préstamo ha sido aprobada.");
        } catch (Exception e) {
            System.err.println("Error al notificar al líder: " + e.getMessage());
        }
    }

    private void updateDeviceStocks(LoanRequest loanRequest) {
        for (LoanRequestDevice loanRequestDevice : loanRequest.getLoanRequestDevices()) {
            LaboratoryDevice device = loanRequestDevice.getDevice();
            device.setQuantity(device.getQuantity() - loanRequestDevice.getQuantity());
            if (device.getQuantity() <= 0) {
                device.setStatus(EDeviceStatus.NO_DISPONIBLE);
            }
            laboratoryDeviceRepository.save(device);
        }
    }


    public void rejectLoanRequest(Long id, Long moderatorId) {
        LoanRequest loanRequest = loanRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan request not found"));

        if (loanRequest.getStatus() != LoanRequestStatus.PENDING) {
            throw new IllegalStateException("Esta solicitud de préstamo ya ha sido procesada y no puede ser rechazada nuevamente.");
        }

        User moderator = userRepository.findById(moderatorId)
                .orElseThrow(() -> new RuntimeException("Moderator not found"));

        loanRequest.setStatus(LoanRequestStatus.REJECTED);
        loanRequest.setRejectedBy(moderator);
        loanRequest.setRejectedAt(OffsetDateTime.now());

        loanRequestRepository.save(loanRequest);

        try {
            notifyLeader(loanRequest, "Solicitud Rechazada", "La solicitud de préstamo ha sido rechazada.");
        } catch (Exception e) {
            System.err.println("Error al notificar al líder: " + e.getMessage());
        }
    }

    // Devolución de dispositivos a la espera de la aprobación por parte del moderador
    public void returnLoanRequest(Long id) {
        LoanRequest loanRequest = loanRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan request not found"));

        if (loanRequest.getStatus() != LoanRequestStatus.APPROVED) {
            throw new IllegalStateException("Los dispositivos no puedes ser devueltos.");
        }

        loanRequest.setStatus(LoanRequestStatus.PENDING_RETURN);
        loanRequest.setReturnAt(OffsetDateTime.now());

        loanRequestRepository.save(loanRequest);

        try {
            notifyModerators(loanRequest, "Solicitud de Devolución ", "Tienes una nueva solicitud de devolución");
        } catch (Exception e) {
            System.err.println("Error al notificar al moderador: " + e.getMessage());
        }
    }

    public void approveReturn(Long id, Long moderatorId) {
        LoanRequest loanRequest = loanRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan request not found"));

        User moderator = userRepository.findById(moderatorId)
                .orElseThrow(() -> new RuntimeException("Moderator not found"));

        if (loanRequest.getStatus() == LoanRequestStatus.PENDING_RETURN && loanRequest.getApprovedBy() == moderator) {
            loanRequest.setStatus(LoanRequestStatus.RETURNED);
            loanRequest.setApproveReturnAt(OffsetDateTime.now());

            // Devolver los dispositivos y actualizar el stock
            for (LoanRequestDevice loanRequestDevice : loanRequest.getLoanRequestDevices()) {
                LaboratoryDevice device = loanRequestDevice.getDevice();
                device.setQuantity(device.getQuantity() + loanRequestDevice.getQuantity());
                device.setStatus(EDeviceStatus.DISPONIBLE); // Cambiar el estado a DISPONIBLE si se devuelve
                laboratoryDeviceRepository.save(device);
            }

            loanRequestRepository.save(loanRequest);
        }
        else {
            throw new IllegalStateException("Error al aprobar devolución.");
        }

    }

    public void rejectReturn(Long id, Long moderatorId) {
        LoanRequest loanRequest = loanRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan request not found"));

        User moderator = userRepository.findById(moderatorId)
                .orElseThrow(() -> new RuntimeException("Moderator not found"));

        if (loanRequest.getStatus() != LoanRequestStatus.PENDING_RETURN) {
            throw new IllegalStateException("Only loan requests pending return can be rejected.");
        }

        loanRequest.setStatus(LoanRequestStatus.RETURN_REJECTED);
        loanRequest.setRejectedReturnAt(OffsetDateTime.now());

        loanRequestRepository.save(loanRequest);
    }

    public void cancelLoanRequest(Long id) {
        LoanRequest loanRequest = loanRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Loan request not found"));

        if (loanRequest.getStatus() != LoanRequestStatus.PENDING) {
            throw new IllegalStateException("Only pending loan requests can be canceled.");
        }
        loanRequest.setStatus(LoanRequestStatus.CANCELED);
        loanRequestRepository.save(loanRequest);
    }

    public List<LoanRequestDTO> getLoanRequestsByProject(Long projectId) {
        List<LoanRequest> loanRequests = loanRequestRepository.findByProjectId(projectId);
        return loanRequests.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private LoanRequestDTO mapToDTO(LoanRequest loanRequest) {
        LoanRequestDTO loanRequestDTO = new LoanRequestDTO();

        // Mapear los campos básicos de LoanRequest
        loanRequestDTO.setId(loanRequest.getId());
        loanRequestDTO.setCreatedAt(loanRequest.getCreatedAt());
        loanRequestDTO.setStatus(loanRequest.getStatus());
        loanRequestDTO.setReservationDate(loanRequest.getReservationDate());
        loanRequestDTO.setTimeSlot(loanRequest.getTimeSlot());
        loanRequestDTO.setApprovedBy(loanRequest.getApprovedBy());
        loanRequestDTO.setApprovedAt(loanRequest.getApprovedAt());
        loanRequestDTO.setRejectedBy(loanRequest.getRejectedBy());
        loanRequestDTO.setRejectedAt(loanRequest.getRejectedAt());
        loanRequestDTO.setReturnAt(loanRequest.getReturnAt());
        loanRequestDTO.setApproveReturnAt(loanRequest.getApproveReturnAt());


        // Mapear la información del proyecto y su líder
        ProjectSimpleDTO projectDTO = new ProjectSimpleDTO();
        projectDTO.setId(loanRequest.getProject().getId());
        projectDTO.setName(loanRequest.getProject().getName());
        projectDTO.setDescription(loanRequest.getProject().getDescription());

        LeaderDTO leaderDTO = new LeaderDTO();
        leaderDTO.setName(loanRequest.getProject().getLeader().getFirstName() + " " + loanRequest.getProject().getLeader().getLastName());
        leaderDTO.setEmail(loanRequest.getProject().getLeader().getEmail());
        projectDTO.setLeader(leaderDTO);

        loanRequestDTO.setProject(projectDTO);

        // Mapear los dispositivos de la solicitud
        Set<LoanRequestDeviceDTO> loanRequestDeviceDTOs = loanRequest.getLoanRequestDevices().stream()
                .map(lrd -> {
                    LoanRequestDeviceDTO deviceDTO = new LoanRequestDeviceDTO();
                    deviceDTO.setId(lrd.getId());
                    deviceDTO.setDeviceId(lrd.getDevice().getId());
                    deviceDTO.setQuantity(lrd.getQuantity());
                    deviceDTO.setDescription(lrd.getDevice().getDescription());
                    return deviceDTO;
                }).collect(Collectors.toSet());

        loanRequestDTO.setLoanRequestDevices(loanRequestDeviceDTOs);

        return loanRequestDTO;
    }
}