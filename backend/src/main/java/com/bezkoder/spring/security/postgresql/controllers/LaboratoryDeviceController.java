package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.dto.laboratorydevice.LaboratoryDeviceDTO;
import com.bezkoder.spring.security.postgresql.models.EDevice;
import com.bezkoder.spring.security.postgresql.models.LaboratoryDevice;
import com.bezkoder.spring.security.postgresql.services.LaboratoryDeviceService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/laboratory-devices")
public class LaboratoryDeviceController {
    @Autowired
    private LaboratoryDeviceService laboratoryDeviceService;

    @GetMapping("/search")
    public List<LaboratoryDeviceDTO> searchDevicesByDescription(@RequestParam String description) {
        return laboratoryDeviceService.searchByDescription(description);
    }

    @GetMapping
    public List<LaboratoryDeviceDTO> getAllDevices() {
        return laboratoryDeviceService.getAllDevices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaboratoryDeviceDTO> getDeviceById(@PathVariable Long id) {
        return ResponseEntity.ok(laboratoryDeviceService.getDeviceById(id));
    }

    @PostMapping
    public ResponseEntity<LaboratoryDeviceDTO> createDevice(@RequestBody LaboratoryDeviceDTO deviceDTO) {
        return ResponseEntity.ok(laboratoryDeviceService.createDevice(deviceDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LaboratoryDeviceDTO> updateDevice(@PathVariable Long id, @RequestBody LaboratoryDeviceDTO deviceDTO) {
        return ResponseEntity.ok(laboratoryDeviceService.updateDevice(id, deviceDTO));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        laboratoryDeviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
