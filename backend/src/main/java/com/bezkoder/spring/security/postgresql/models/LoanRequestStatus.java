package com.bezkoder.spring.security.postgresql.models;

public enum LoanRequestStatus {
    PENDING,       // Solicitud pendiente de revisión
    APPROVED,      // Solicitud aprobada
    REJECTED,      // Solicitud rechazada
    PENDING_RETURN, // Dispositivos en proceso de devolución
    RETURNED,      // Dispositivos devueltos y aprobados
    RETURN_REJECTED, // Devolución rechazada
    CANCELED       // Solicitud cancelada
}