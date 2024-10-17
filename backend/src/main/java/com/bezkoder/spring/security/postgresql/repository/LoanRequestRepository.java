package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.Invitation;
import com.bezkoder.spring.security.postgresql.models.LoanRequest;
import com.bezkoder.spring.security.postgresql.models.LoanRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, Long> {
    // Obtener todas las solicitudes de préstamo de un usuario específico
    List<LoanRequest> findByProjectLeaderId(Long leaderId);
    List<LoanRequest> findByProjectId(Long projectId);
    Optional<LoanRequest> findByIdAndStatus(Long id, LoanRequestStatus status);
    List<LoanRequest> findByStatus(String status);

    // Verificar si existe una solicitud en una fecha y horario específicos
    boolean existsByReservationDateAndTimeSlot(LocalDate date, String timeSlot);
}
