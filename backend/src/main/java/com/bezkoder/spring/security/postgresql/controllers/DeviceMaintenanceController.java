package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.dto.DeviceMaintenanceDTO;
import com.bezkoder.spring.security.postgresql.services.DeviceMaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/device-maintenance")
public class DeviceMaintenanceController {

    @Autowired
    private DeviceMaintenanceService maintenanceService;

    @GetMapping
    public List<DeviceMaintenanceDTO> getAllMaintenanceRecords() {
        return maintenanceService.getAllMaintenanceRecords();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceMaintenanceDTO> getMaintenanceRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(maintenanceService.getMaintenanceRecordById(id));
    }

    @PostMapping
    public ResponseEntity<DeviceMaintenanceDTO> createMaintenanceRecord(@RequestBody DeviceMaintenanceDTO maintenanceDTO) {
        return ResponseEntity.ok(maintenanceService.createMaintenanceRecord(maintenanceDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceRecord(@PathVariable Long id) {
        maintenanceService.deleteMaintenanceRecord(id);
        return ResponseEntity.noContent().build();
    }
}