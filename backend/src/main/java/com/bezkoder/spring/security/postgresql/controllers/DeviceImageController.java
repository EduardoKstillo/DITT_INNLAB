package com.bezkoder.spring.security.postgresql.controllers;

import com.bezkoder.spring.security.postgresql.dto.DeviceImageDTO;
import com.bezkoder.spring.security.postgresql.services.DeviceImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/device-images")
public class DeviceImageController {

    private final DeviceImageService deviceImageService;

    @Autowired
    public DeviceImageController(DeviceImageService deviceImageService) {
        this.deviceImageService = deviceImageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<DeviceImageDTO> uploadImage(@RequestParam Long deviceId,
                                                      @RequestParam MultipartFile file) {
        try {
            DeviceImageDTO deviceImageDTO = deviceImageService.createImage(deviceId, file);
            return new ResponseEntity<>(deviceImageDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/upload-multiple")
    public ResponseEntity<List<DeviceImageDTO>> uploadImages(@RequestParam Long deviceId,
                                                             @RequestParam MultipartFile[] files) {
        try {
            List<DeviceImageDTO> deviceImageDTOs = deviceImageService.createImages(deviceId, files);
            return new ResponseEntity<>(deviceImageDTOs, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<DeviceImageDTO>> getAllImages() {
        List<DeviceImageDTO> deviceImageDTOs = deviceImageService.getAllImages();
        return new ResponseEntity<>(deviceImageDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceImageDTO> getImageById(@PathVariable Long id) {
        Optional<DeviceImageDTO> deviceImageDTO = Optional.ofNullable(deviceImageService.getImageById(id));
        return deviceImageDTO.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        try {
            deviceImageService.deleteImage(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}