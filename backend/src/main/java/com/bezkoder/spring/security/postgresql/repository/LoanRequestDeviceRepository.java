package com.bezkoder.spring.security.postgresql.repository;

import com.bezkoder.spring.security.postgresql.models.LoanRequestDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRequestDeviceRepository extends JpaRepository<LoanRequestDevice, Long> {
}