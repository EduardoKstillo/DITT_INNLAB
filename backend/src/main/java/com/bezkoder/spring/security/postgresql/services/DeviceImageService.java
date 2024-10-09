package com.bezkoder.spring.security.postgresql.services;

import com.bezkoder.spring.security.postgresql.dto.DeviceImageDTO;
import com.bezkoder.spring.security.postgresql.models.DeviceImage;
import com.bezkoder.spring.security.postgresql.models.LaboratoryDevice;
import com.bezkoder.spring.security.postgresql.repository.DeviceImageRepository;
import com.bezkoder.spring.security.postgresql.repository.LaboratoryDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceImageService {

    private final DeviceImageRepository deviceImageRepository;
    private final LaboratoryDeviceRepository laboratoryDeviceRepository;
    private final Path rootLocation;

    @Value("${file.upload-dir}") // Ruta de la carpeta de imágenes
    private String uploadDir;

    @Autowired
    public DeviceImageService(DeviceImageRepository deviceImageRepository,
                              LaboratoryDeviceRepository laboratoryDeviceRepository,
                              @Value("${file.upload-dir}") String uploadDir) {
        this.deviceImageRepository = deviceImageRepository;
        this.laboratoryDeviceRepository = laboratoryDeviceRepository;
        this.rootLocation = Paths.get(uploadDir);

        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo inicializar el almacenamiento", e);
        }
    }

    // Método para crear una imagen a partir de un archivo multipart
    public DeviceImageDTO createImage(Long deviceId, MultipartFile file) {
        try {
            LaboratoryDevice device = laboratoryDeviceRepository.findById(deviceId)
                    .orElseThrow(() -> new RuntimeException("Dispositivo no encontrado"));

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path destinationFile = Paths.get(uploadDir).resolve(Paths.get(fileName))
                    .normalize().toAbsolutePath();

            // Guardar el archivo en la carpeta especificada
            file.transferTo(destinationFile.toFile());

            DeviceImage deviceImage = new DeviceImage();
            deviceImage.setDevice(device);
            deviceImage.setImageUrl(destinationFile.toString());
            deviceImageRepository.save(deviceImage);

            return mapToDTO(deviceImage);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }
    }

    // Método para crear múltiples imágenes
    public List<DeviceImageDTO> createImages(Long deviceId, MultipartFile[] files) {
        LaboratoryDevice device = laboratoryDeviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Dispositivo no encontrado"));

        return List.of(files).stream().map(file -> {
            try {
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path destinationFile = Paths.get(uploadDir).resolve(Paths.get(fileName))
                        .normalize().toAbsolutePath();

                // Guardar el archivo en la carpeta especificada
                file.transferTo(destinationFile.toFile());

                DeviceImage deviceImage = new DeviceImage();
                deviceImage.setDevice(device);
                deviceImage.setImageUrl(destinationFile.toString());

                return mapToDTO(deviceImageRepository.save(deviceImage));
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar archivo: " + file.getOriginalFilename(), e);
            }
        }).collect(Collectors.toList());
    }

    // Obtener todas las imágenes
    public List<DeviceImageDTO> getAllImages() {
        return deviceImageRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Obtener una imagen por su ID
    public DeviceImageDTO getImageById(Long id) {
        DeviceImage image = deviceImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
        return mapToDTO(image);
    }

    // Eliminar una imagen
    public void deleteImage(Long id) {
        DeviceImage deviceImage = deviceImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        try {
            Files.deleteIfExists(Paths.get(deviceImage.getImageUrl()));
            deviceImageRepository.deleteById(id);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar el archivo", e);
        }
    }

    // Método para mapear DeviceImage a DeviceImageDTO
    private DeviceImageDTO mapToDTO(DeviceImage deviceImage) {
        DeviceImageDTO dto = new DeviceImageDTO();
        dto.setId(deviceImage.getId());
        dto.setImagePath(deviceImage.getImageUrl());
        dto.setDeviceId(deviceImage.getDevice().getId());
        return dto;
    }

    // Método para mapear DeviceImageDTO a DeviceImage (si se necesitara)
    private DeviceImage mapToEntity(DeviceImageDTO dto) {
        DeviceImage image = new DeviceImage();
        image.setImageUrl(dto.getImagePath());
        // Agregar cualquier mapeo adicional si se requiere
        return image;
    }
}