package com.bezkoder.spring.security.postgresql.services;

import com.bezkoder.spring.security.postgresql.dto.laboratorydevice.LaboratoryDeviceDTO;
import com.bezkoder.spring.security.postgresql.models.EDeviceStatus;
import com.bezkoder.spring.security.postgresql.models.LaboratoryDevice;
import com.bezkoder.spring.security.postgresql.repository.LaboratoryDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LaboratoryDeviceService {

    @Autowired
    private LaboratoryDeviceRepository repository;

    // Método para obtener dispositivos que coincidan con una descripción parcial
    public List<LaboratoryDeviceDTO> searchByDescription(String description) {
        List<LaboratoryDevice> devices = repository.findByDescriptionContainingIgnoreCase(description);
        return devices.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<LaboratoryDeviceDTO> getAllDevices() {
        return repository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public LaboratoryDeviceDTO getDeviceById(Long id) {
        LaboratoryDevice device = repository.findById(id).orElseThrow(() -> new RuntimeException("Device not found"));
        return mapToDTO(device);
    }

    public LaboratoryDeviceDTO createDevice(LaboratoryDeviceDTO dto) {
        LaboratoryDevice device = mapToEntity(dto);
        repository.save(device);
        return mapToDTO(device);
    }

    public LaboratoryDevice save(LaboratoryDevice device) {
        return repository.save(device);
    }

    public LaboratoryDeviceDTO updateDevice(Long id, LaboratoryDeviceDTO dto) {
        LaboratoryDevice device = repository.findById(id).orElseThrow(() -> new RuntimeException("Device not found"));
        device.setType(dto.getType());
        device.setDescription(dto.getDescription());
        device.setCharacteristics(dto.getCharacteristics());
        device.setSeries(dto.getSeries());
        device.setQuantity(dto.getQuantity());
        device.setAdditional(dto.getAdditional());
        device.setColor(dto.getColor());
        device.setStatus(dto.getStatus());
        repository.save(device);
        return mapToDTO(device);
    }

    public void deleteDevice(Long id) {
        repository.deleteById(id);
    }

    public LaboratoryDevice findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Device not found"));
    }

    public List<LaboratoryDevice> findByDescription(String description) {
        return repository.findByDescriptionContaining(description);
    }

    // Mapping methods
    private LaboratoryDeviceDTO mapToDTO(LaboratoryDevice device) {
        LaboratoryDeviceDTO dto = new LaboratoryDeviceDTO();
        dto.setId(device.getId());
        dto.setType(device.getType());
        dto.setDescription(device.getDescription());
        dto.setCharacteristics(device.getCharacteristics());
        dto.setSeries(device.getSeries());
        dto.setQuantity(device.getQuantity());
        dto.setAdditional(device.getAdditional());
        dto.setColor(device.getColor());
        dto.setStatus(device.getStatus());
        return dto;
    }

    private LaboratoryDevice mapToEntity(LaboratoryDeviceDTO dto) {
        LaboratoryDevice device = new LaboratoryDevice();
        device.setType(dto.getType());
        device.setDescription(dto.getDescription());
        device.setCharacteristics(dto.getCharacteristics());
        device.setSeries(dto.getSeries());
        device.setQuantity(dto.getQuantity());
        device.setAdditional(dto.getAdditional());
        device.setColor(dto.getColor());
        device.setStatus(EDeviceStatus.DISPONIBLE);
        return device;
    }
}