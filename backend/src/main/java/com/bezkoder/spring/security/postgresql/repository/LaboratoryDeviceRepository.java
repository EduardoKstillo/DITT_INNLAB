package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.LaboratoryDevice;
import com.bezkoder.spring.security.postgresql.models.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LaboratoryDeviceRepository extends JpaRepository<LaboratoryDevice, Long> {
    List<LaboratoryDevice> findByDescriptionContaining(String description);

    List<LaboratoryDevice> findByDescriptionContainingIgnoreCase(String description);

}
