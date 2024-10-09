import { Component, OnInit } from '@angular/core';
import { LaboratoryDeviceService } from '../../services/laboratory-device.service';
import { Router } from '@angular/router';
import { AlertController } from '@ionic/angular';

@Component({
  selector: 'app-device-list',
  templateUrl: './device-list.page.html',
  styleUrls: ['./device-list.page.scss'],
})
export class DeviceListPage implements OnInit {
  devices: any[] = []; // Almacena la lista de dispositivos

  constructor(
    private deviceService: LaboratoryDeviceService,
    private router: Router,
    private alertController: AlertController
  ) {}

  ngOnInit() {
    this.loadDevices();
  }

  ionViewWillEnter() {
    this.loadDevices();  // También recargar los dispositivos cuando entras a la página
  }

  loadDevices() {
    this.deviceService.getAllDevices().subscribe(
      (response) => {
        this.devices = response; // Asignar la respuesta a la variable
      },
      (error) => {
        console.error('Error fetching devices:', error);
      }
    );
  }

  async deleteDevice(deviceId: number) {
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
            this.deviceService.deleteDevice(deviceId).subscribe(
              () => {
                this.loadDevices(); // Recargar la lista de dispositivos
              },
              (error) => {
                console.error('Error deleting device:', error);
              }
            );
          },
        },
      ],
    });

    await alert.present();
  }

  navigateToCreate() {
    this.router.navigate(['/device-create']); // Navegar a la página de creación de dispositivos
  }

  navigateToDetail(deviceId: number) {
    this.router.navigate(['/device-detail', deviceId]); // Navegar a la página de detalle
  }
  navigateToUpdate(deviceId: number) {
    this.router.navigate(['/device-update', deviceId]); // Navegar a la página de detalle
  }
}
