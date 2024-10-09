package com.bezkoder.spring.security.postgresql.services;

import com.bezkoder.spring.security.postgresql.dto.DeviceMaintenanceDTO;
import com.bezkoder.spring.security.postgresql.models.DeviceMaintenance;
import com.bezkoder.spring.security.postgresql.models.EDeviceStatus;
import com.bezkoder.spring.security.postgresql.models.LaboratoryDevice;
import com.bezkoder.spring.security.postgresql.repository.DeviceMaintenanceRepository;
import com.bezkoder.spring.security.postgresql.repository.LaboratoryDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceMaintenanceService {

    @Autowired
    private DeviceMaintenanceRepository repository;

    @Autowired
    private LaboratoryDeviceService deviceService;

    public List<DeviceMaintenanceDTO> getAllMaintenanceRecords() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public DeviceMaintenanceDTO getMaintenanceRecordById(Long id) {
        DeviceMaintenance maintenance = repository.findById(id).orElseThrow(() -> new RuntimeException("Maintenance record not found"));
        return mapToDTO(maintenance);
    }

    public DeviceMaintenanceDTO createMaintenanceRecord(DeviceMaintenanceDTO dto) {
        DeviceMaintenance maintenance = mapToEntity(dto);
        repository.save(maintenance);

        // Actualizar el estado del dispositivo
        LaboratoryDevice device = deviceService.findById(dto.getDeviceId());
        device.setStatus(EDeviceStatus.MANTENIMIENTO);
        deviceService.save(device);
        return mapToDTO(maintenance);
    }

    public void deleteMaintenanceRecord(Long id) {
        DeviceMaintenance maintenance = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance record not found"));
        // Volver a actualizar el estado del dispositivo si es necesario
        LaboratoryDevice device = maintenance.getDevice();
        device.setStatus(EDeviceStatus.DISPONIBLE); // O el estado que consideres apropiado
        deviceService.save(device);
        repository.deleteById(id);
    }


    private DeviceMaintenanceDTO mapToDTO(DeviceMaintenance maintenance) {
        DeviceMaintenanceDTO dto = new DeviceMaintenanceDTO();
        dto.setId(maintenance.getId());
        dto.setDeviceId(maintenance.getDevice().getId());
        dto.setMaintenanceDate(maintenance.getMaintenanceDate());
        dto.setDetails(maintenance.getDetails());
        dto.setExpectedReturnDate(maintenance.getExpectedReturnDate());
        return dto;
    }

    private DeviceMaintenance mapToEntity(DeviceMaintenanceDTO dto) {
        DeviceMaintenance maintenance = new DeviceMaintenance();
        maintenance.setMaintenanceDate(dto.getMaintenanceDate());
        maintenance.setDetails(dto.getDetails());
        maintenance.setExpectedReturnDate(dto.getExpectedReturnDate());
        LaboratoryDevice device = deviceService.findById(dto.getDeviceId());
        maintenance.setDevice(device);
        return maintenance;
    }
}