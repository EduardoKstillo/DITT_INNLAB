package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.dto.LoanRequestDeviceDTO;
import com.bezkoder.spring.security.postgresql.services.LoanRequestDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/loan-request-devices")
public class LoanRequestDeviceController {

    private final LoanRequestDeviceService loanRequestDeviceService;

    @Autowired
    public LoanRequestDeviceController(LoanRequestDeviceService loanRequestDeviceService) {
        this.loanRequestDeviceService = loanRequestDeviceService;
    }

    @PostMapping
    public ResponseEntity<LoanRequestDeviceDTO> createLoanRequestDevice(@RequestBody LoanRequestDeviceDTO loanRequestDeviceDTO) {
        LoanRequestDeviceDTO createdLoanRequestDevice = loanRequestDeviceService.createLoanRequestDevice(loanRequestDeviceDTO);
        return ResponseEntity.ok(createdLoanRequestDevice);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanRequestDeviceDTO> getLoanRequestDeviceById(@PathVariable Long id) {
        LoanRequestDeviceDTO loanRequestDeviceDTO = loanRequestDeviceService.getLoanRequestDeviceById(id);
        return ResponseEntity.ok(loanRequestDeviceDTO);
    }

    @GetMapping
    public ResponseEntity<List<LoanRequestDeviceDTO>> getAllLoanRequestDevices() {
        List<LoanRequestDeviceDTO> loanRequestDeviceDTOs = loanRequestDeviceService.getAllLoanRequestDevices();
        return ResponseEntity.ok(loanRequestDeviceDTOs);
    }
}