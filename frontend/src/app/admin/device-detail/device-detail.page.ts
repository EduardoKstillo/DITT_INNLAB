import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LaboratoryDeviceService } from '../../services/laboratory-device.service';
import { AlertController } from '@ionic/angular';

@Component({
  selector: 'app-device-detail',
  templateUrl: './device-detail.page.html',
  styleUrls: ['./device-detail.page.scss'],
})
export class DeviceDetailPage implements OnInit {
  deviceId: number | null = null; // Inicializado como null
  device: any;

  constructor(
    private route: ActivatedRoute,
    private deviceService: LaboratoryDeviceService,
    private alertController: AlertController,
    private router: Router
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id'); // Obtener el ID como string
    if (id) {
      this.deviceId = +id; // Convertirlo a número solo si no es null
      this.loadDeviceDetails(); // Cargar detalles solo si hay un ID válido
    } else {
      console.error('Device ID is null or undefined.');
      this.router.navigate(['/device-list']); // Redirigir si no hay ID
    }
  }

  loadDeviceDetails() {
    if (this.deviceId !== null) { // Verificar que deviceId no sea null
      this.deviceService.getDeviceById(this.deviceId).subscribe(
        (response) => {
          this.device = response;
        },
        (error) => {
          console.error('Error fetching device details:', error);
        }
      );
    } else {
      console.error('Device ID is null or undefined. Cannot fetch device details.');
    }
  }

  async deleteDevice() {
    const alert = await this.alertController.create({
      header: 'Confirmar',
      message: '¿Estás seguro de que deseas eliminar este dispositivo?',
      buttons: [
        {
          text: 'Cancelar',
          role: 'cancel',
          cssClass: 'secondary',
        },
        {
          text: 'Eliminar',
          handler: () => {
            if (this.deviceId) { // Asegúrate de que deviceId tenga un valor
              this.deviceService.deleteDevice(this.deviceId).subscribe(
                async () => {
                  const alert = await this.alertController.create({
                    header: 'Éxito',
                    message: 'Dispositivo eliminado con éxito.',
                    buttons: ['OK']
                  });
                  await alert.present();
                  this.router.navigate(['/device-list']);
                },
                async (error) => {
                  const alert = await this.alertController.create({
                    header: 'Error',
                    message: 'Error al eliminar el dispositivo.',
                    buttons: ['OK']
                  });
                  await alert.present();
                }
              );
            }
          },
        },
      ],
    });

    await alert.present();
  }

  navigateToUpdate() {
    if (this.deviceId) {
      this.router.navigate(['/device-update', this.deviceId]);
    }
  }
}
