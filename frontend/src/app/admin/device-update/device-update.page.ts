import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LaboratoryDeviceService } from '../../services/laboratory-device.service';
import { AlertController } from '@ionic/angular';

@Component({
  selector: 'app-device-update',
  templateUrl: './device-update.page.html',
  styleUrls: ['./device-update.page.scss'],
})
export class DeviceUpdatePage implements OnInit {
  deviceId: number | null = null; // Inicializado como null
  deviceData: any = {};

  constructor(
    private route: ActivatedRoute,
    private deviceService: LaboratoryDeviceService,
    private alertController: AlertController,
    private router: Router
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id'); // Obtener el ID como string
    if (id) {
      this.deviceId = +id; // Convertirlo a número
      this.loadDeviceDetails(); // Cargar detalles del dispositivo
    } else {
      console.error('Device ID is null or undefined. Redirecting to device list.');
      this.router.navigate(['/device-list']); // Redirigir si no hay ID
    }
  }

  loadDeviceDetails() {
    if (this.deviceId) { // Verificar que deviceId tiene un valor
      this.deviceService.getDeviceById(this.deviceId).subscribe(
        (response) => {
          this.deviceData = response; // Asignar la respuesta a la variable
        },
        (error) => {
          console.error('Error fetching device details:', error);
        }
      );
    }
  }

  async updateDevice() {
    if (this.deviceId) { // Verificar que deviceId tiene un valor
      this.deviceService.updateDevice(this.deviceId, this.deviceData).subscribe(
        async () => {
          const alert = await this.alertController.create({
            header: 'Éxito',
            message: 'Dispositivo actualizado con éxito.',
            buttons: ['OK']
          });
          await alert.present();
          this.router.navigate(['/device-list']); // Regresar a la lista de dispositivos
        },
        async (error) => {
          const alert = await this.alertController.create({
            header: 'Error',
            message: 'Error al actualizar el dispositivo.',
            buttons: ['OK']
          });
          await alert.present();
        }
      );
    }
  }
}
