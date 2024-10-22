import { Component } from '@angular/core';
import { LaboratoryDeviceService } from '../../services/laboratory-device.service';
import { Router } from '@angular/router';
import { AlertController } from '@ionic/angular';

@Component({
  selector: 'app-device-create',
  templateUrl: './device-create.page.html',
  styleUrls: ['./device-create.page.scss'],
})
export class DeviceCreatePage {
  deviceData = {
    type: '',
    description: '',
    characteristics: '',
    series: '',
    quantity: 0,
    additional: '',
    sensorType: '',
    color: '',
    status: 'DISPONIBLE'
  };

  constructor(
    private deviceService: LaboratoryDeviceService,
    private router: Router,
    private alertController: AlertController
  ) {}

  async createDevice() {
    this.deviceService.createDevice(this.deviceData).subscribe(
      async () => {
        const alert = await this.alertController.create({
          header: 'Éxito',
          message: 'Dispositivo creado con éxito.',
          buttons: ['OK']
        });
        await alert.present();

        // Restablecer el formulario después de la creación exitosa
        this.resetForm();

        // Navegar de regreso a la lista de dispositivos
        this.router.navigate(['/device-list']);
      },
      async (error) => {
        const alert = await this.alertController.create({
          header: 'Error',
          message: 'Error al crear el dispositivo.',
          buttons: ['OK']
        });
        await alert.present();
      }
    );
  }

  onTypeChange(selectedType: string) {
    // Si cambia a un tipo que no sea IoT, limpia los campos específicos de IoT
    if (selectedType !== 'IoT') {
      this.deviceData.sensorType = ''; // Limpia el campo de tipo de sensor
    }
    
    // Si cambia a IoT, limpia los campos específicos de otros tipos
    if (selectedType === 'IoT') {
      this.deviceData.series = '';
      this.deviceData.color = '';
      this.deviceData.additional = '';
    } else {
      // Si es Mobile o Metaverso, limpia el campo de sensorType
      this.deviceData.sensorType = '';
    }
  }
  

  // Función para resetear el formulario
  resetForm() {
    this.deviceData = {
      type: '',
      description: '',
      characteristics: '',
      series: '',
      quantity: 0,
      sensorType: '',
      additional: '',
      color: '',
      status: 'DISPONIBLE'
    };
  }
}
