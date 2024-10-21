package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.dto.LoanRequest2DTO;
import com.bezkoder.spring.security.postgresql.dto.LoanRequestDTO;
import com.bezkoder.spring.security.postgresql.services.LoanRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/loan-requests")
public class LoanRequestController {

    private final LoanRequestService loanRequestService;

    @Autowired
    public LoanRequestController(LoanRequestService loanRequestService) {
        this.loanRequestService = loanRequestService;
    }

    // Crear una nueva solicitud de préstamo
    @PostMapping
    public ResponseEntity<LoanRequestDTO> createLoanRequest(@RequestBody LoanRequest2DTO loanRequestDTO) {
        LoanRequestDTO createdLoanRequest = loanRequestService.createLoanRequest(loanRequestDTO);
        return new ResponseEntity<>(createdLoanRequest, HttpStatus.CREATED);
    }

    // Obtener una solicitud de préstamo por ID
    @GetMapping("/{id}")
    public ResponseEntity<LoanRequestDTO> getLoanRequestById(@PathVariable Long id) {
        LoanRequestDTO loanRequestDTO = loanRequestService.getLoanRequestById(id);
        return ResponseEntity.ok(loanRequestDTO);
    }

    // Obtener todas las solicitudes de préstamo
    @GetMapping
    public ResponseEntity<List<LoanRequestDTO>> getAllLoanRequests() {
        List<LoanRequestDTO> loanRequests = loanRequestService.getAllLoanRequests();
        return ResponseEntity.ok(loanRequests);
    }

    // Obtener solicitudes de préstamo por estado (PENDIENTE, APROBADA, RECHAZADA)
    @GetMapping("/status/{status}")
    public ResponseEntity<List<LoanRequestDTO>> getLoanRequestsByStatus(@PathVariable String status) {
        List<LoanRequestDTO> loanRequests = loanRequestService.getLoanRequestsByStatus(status);
        return ResponseEntity.ok(loanRequests);
    }

    // Aprobar una solicitud de préstamo
    @PatchMapping("/{id}/approve")
    public ResponseEntity<Void> approveLoanRequest(@PathVariable Long id, @RequestParam Long moderatorId) {
        loanRequestService.approveLoanRequest(id, moderatorId);
        return ResponseEntity.ok().build();
    }

    // Rechazar una solicitud de préstamo
    @PatchMapping("/{id}/reject")
    public ResponseEntity<Void> rejectLoanRequest(@PathVariable Long id, @RequestParam Long moderatorId) {
        loanRequestService.rejectLoanRequest(id, moderatorId);
        return ResponseEntity.ok().build();
    }

    // Iniciar la solicitud de devolución de un préstamo aprobado
    @PatchMapping("/{id}/return")
    public ResponseEntity<Void> returnLoanRequest(@PathVariable Long id) {
        loanRequestService.returnLoanRequest(id);
        return ResponseEntity.ok().build();
    }

    // Aprobar la devolución de los dispositivos
    @PatchMapping("/{id}/approve-return")
    public ResponseEntity<Void> approveReturn(@PathVariable Long id, @RequestParam Long moderatorId) {
        loanRequestService.approveReturn(id, moderatorId);
        return ResponseEntity.ok().build();
    }

    // Rechazar la devolución de una solicitud de préstamo
    @PatchMapping("/{id}/reject-return")
    public ResponseEntity<Void> rejectReturn(@PathVariable Long id, @RequestParam Long moderatorId) {
        loanRequestService.rejectReturn(id, moderatorId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Cancelar una solicitud de préstamo
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<String> cancelLoanRequest(@PathVariable Long id) {
        try {
            loanRequestService.cancelLoanRequest(id);
            return ResponseEntity.ok("Loan request canceled successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Obtener solicitudes de préstamo por proyecto
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<LoanRequestDTO>> getLoanRequestsByProject(@PathVariable Long projectId) {
        List<LoanRequestDTO> loanRequests = loanRequestService.getLoanRequestsByProject(projectId);
        return ResponseEntity.ok(loanRequests);
    }
}
