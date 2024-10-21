package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Invitation;
import com.bezkoder.spring.security.postgresql.models.LoanRequest;
import com.bezkoder.spring.security.postgresql.models.LoanRequestStatus;
import com.bezkoder.spring.security.postgresql.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {
    // Obtener todas las solicitudes de préstamo de un usuario específico
    List<LoanRequest> findByProjectLeaderId(Long leaderId);
    List<LoanRequest> findByProjectId(Long projectId);
    Optional<LoanRequest> findByIdAndStatus(Long id, LoanRequestStatus status);
    List<LoanRequest> findByStatus(String status);

    // Consulta para verificar si ya existe una solicitud APROBADA en el mismo horario para un proyecto
    boolean existsByReservationDateAndTimeSlotAndStatus(LocalDate reservationDate, String timeSlot, LoanRequestStatus status);

    // Consulta para verificar si ya existe una solicitud APROBADA en el mismo horario para un proyecto diferente
    boolean existsByReservationDateAndTimeSlotAndStatusAndProjectIdNot(
            LocalDate reservationDate, String timeSlot, LoanRequestStatus status, Long projectId);

}
