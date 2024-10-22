package com.bezkoder.spring.security.postgresql.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "laboratory_devices")
public class
LaboratoryDevice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @NotNull
  private EDevice type;

  @NotBlank
  private String description;

  @NotBlank
  private String characteristics;

  @NotBlank
  private String series;

  @NotNull
  private Integer quantity;

  @NotBlank
  private String additional;

  @NotBlank
  private String sensorType;

  @NotBlank
  private String color;

  @Enumerated(EnumType.STRING)
  @NotNull
  private EDeviceStatus status;

  // Relación con las imágenes del dispositivo
  @OneToMany(mappedBy = "device", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<DeviceImage> images = new ArrayList<>();

  @OneToMany(mappedBy = "device")
  private List<DeviceMaintenance> maintenanceRecords = new ArrayList<>();

  // Métodos para manejar stock
  public void reduceStock(int quantity) throws IllegalArgumentException {
    if (this.quantity >= quantity) {
      this.quantity -= quantity;
    } else {
      throw new IllegalArgumentException("Stock insuficiente para este dispositivo.");
    }
  }

  public void increaseStock(int quantity) {
    this.quantity += quantity;
  }

  public void addImage(DeviceImage image) {
    images.add(image);
    image.setDevice(this);
  }

  public void removeImage(DeviceImage image) {
    images.remove(image);
    image.setDevice(null);
  }

  public boolean isAvailable() {
    return this.quantity > 0 && this.status == EDeviceStatus.DISPONIBLE;
  }
}