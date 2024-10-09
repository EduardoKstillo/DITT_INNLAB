package com.bezkoder.spring.security.postgresql.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "loan_request_devices")
public class LoanRequestDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "loan_request_id")
    @JsonIgnore
    private LoanRequest loanRequest;

    @ManyToOne
    @JoinColumn(name = "laboratory_device_id")
    @JsonIgnore
    private LaboratoryDevice device;

    private int quantity;
}