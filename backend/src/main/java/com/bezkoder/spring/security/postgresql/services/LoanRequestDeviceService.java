package com.bezkoder.spring.security.postgresql.services;

import com.bezkoder.spring.security.postgresql.dto.LoanRequestDeviceDTO;
import com.bezkoder.spring.security.postgresql.models.LaboratoryDevice;
import com.bezkoder.spring.security.postgresql.models.LoanRequest;
import com.bezkoder.spring.security.postgresql.models.LoanRequestDevice;
import com.bezkoder.spring.security.postgresql.repository.LaboratoryDeviceRepository;
import com.bezkoder.spring.security.postgresql.repository.LoanRequestDeviceRepository;
import com.bezkoder.spring.security.postgresql.repository.LoanRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoanRequestDeviceService {

    private final LoanRequestDeviceRepository loanRequestDeviceRepository;
    private final LoanRequestRepository loanRequestRepository;
    private final LaboratoryDeviceRepository laboratoryDeviceRepository;

    @Autowired
    public LoanRequestDeviceService(
            LoanRequestDeviceRepository loanRequestDeviceRepository,
            LoanRequestRepository loanRequestRepository,
            LaboratoryDeviceRepository laboratoryDeviceRepository) {
        this.loanRequestDeviceRepository = loanRequestDeviceRepository;
        this.loanRequestRepository = loanRequestRepository;
        this.laboratoryDeviceRepository = laboratoryDeviceRepository;
    }

    public LoanRequestDeviceDTO createLoanRequestDevice(LoanRequestDeviceDTO loanRequestDeviceDTO) {
        // Retrieve associated entities
        LoanRequest loanRequest = loanRequestRepository.findById(loanRequestDeviceDTO.getLoanRequestId())
                .orElseThrow(() -> new RuntimeException("Loan request not found"));
        LaboratoryDevice device = laboratoryDeviceRepository.findById(loanRequestDeviceDTO.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Device not found"));

        // Create and populate the LoanRequestDevice entity
        LoanRequestDevice loanRequestDevice = new LoanRequestDevice();
        loanRequestDevice.setLoanRequest(loanRequest);
        loanRequestDevice.setDevice(device);
        loanRequestDevice.setQuantity(loanRequestDeviceDTO.getQuantity());

        loanRequestDevice = loanRequestDeviceRepository.save(loanRequestDevice);

        // Return DTO
        return mapToDTO(loanRequestDevice);
    }

    public LoanRequestDeviceDTO getLoanRequestDeviceById(Long id) {
        Optional<LoanRequestDevice> loanRequestDevice = loanRequestDeviceRepository.findById(id);
        return loanRequestDevice.map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Loan request device not found"));
    }

    public List<LoanRequestDeviceDTO> getAllLoanRequestDevices() {
        return loanRequestDeviceRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private LoanRequestDeviceDTO mapToDTO(LoanRequestDevice loanRequestDevice) {
        LoanRequestDeviceDTO dto = new LoanRequestDeviceDTO();

        // Map basic fields
        dto.setId(loanRequestDevice.getId());
        if (loanRequestDevice.getLoanRequest() != null) {
            dto.setLoanRequestId(loanRequestDevice.getLoanRequest().getId());
        }
        if (loanRequestDevice.getDevice() != null) {
            dto.setDeviceId(loanRequestDevice.getDevice().getId());
        }
        dto.setQuantity(loanRequestDevice.getQuantity());

        return dto;
    }
}