import { Component, Input } from '@angular/core';
import { AlertController, ModalController } from '@ionic/angular';
import { LoanRequestService } from '../services/loan-request.service';
import { LaboratoryDeviceService } from '../services/laboratory-device.service';
import { CardModalComponent } from '../components/card-modal/card-modal.component';
import { ActivatedRoute } from '@angular/router';

interface Device {
  id: number;
  type: string;
  description: string;
  characteristics: string;
  series: string;
  quantity: number;
  additional: string;
  color: string;
  status: string;
  selectedQuantity?: number; // Propiedad opcional
}

@Component({
  selector: 'app-request-loan',
  templateUrl: './request-loan.page.html',
  styleUrls: ['./request-loan.page.scss'],
})
export class RequestLoanPage {
  projectId?: number; // ID del proyecto para la solicitud de préstamo
  devices: any[] = []; // Dispositivos disponibles
  cart: Device[] = []; // Dispositivos agregados al carrito
  isModalOpen = false;

  loanPurpose: string = '';
  filteredDevices: any[] = [];
  searchQuery: string = '';

  constructor(
    private loanRequestService: LoanRequestService,
    private laboratoryDeviceService: LaboratoryDeviceService,
    private alertController: AlertController,
    private modalController: ModalController,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    // Obtener el ID del proyecto desde la URL
    this.route.paramMap.subscribe(params => {
      this.projectId = +params.get('projectId')!; 
      console.log('Project ID:', this.projectId); 
    });
    // Cargar la lista de dispositivos disponibles al abrir el modal
    this.loadAvailableDevices();
  }

  loadAvailableDevices() {
    this.laboratoryDeviceService.getAllDevices().subscribe(
      (devices) => {
        this.devices = devices.map((device: Device) => ({
          ...device,
          selectedQuantity: 0  // Inicializa con 0 o un valor por defecto
        }));
      },
      (error) => {
        console.error('Error loading devices:', error);
      }
    );
  }

  onSearchInput(event: any) {
    const query = event.target.value.toLowerCase();

    if (query && query.trim() !== '') {
      // Filtrar dispositivos por la propiedad description
      this.filteredDevices = this.devices.filter(device => 
        device.description.toLowerCase().includes(query)
      );
    } else {
      // Si el campo de búsqueda está vacío, mostrar todos los dispositivos
      this.filteredDevices = this.devices;
    }
  }


  // Agregar dispositivo al carrito
  addToCart(device: any) {
    if (device.selectedQuantity > 0) {
      const index = this.cart.findIndex((d) => d.id === device.id);
      if (index === -1) {
        this.cart.push({ ...device, selectedQuantity: device.selectedQuantity || 0 });
      } else {
        // Actualizar la cantidad si ya está en el carrito
        this.cart[index].selectedQuantity = device.selectedQuantity;
      }
    } else {
      this.presentAlert('Por favor, selecciona una cantidad válida antes de agregar.');
    }
  }

  removeFromCart(device: Device) {
    // Filtrar el dispositivo a eliminar del carrito
    this.cart = this.cart.filter(d => d.id !== device.id);
  }

  onQuantityChangeInCart(device: Device, newQuantity: number) {
    if (newQuantity === 0) {
      // Si la cantidad es 0, eliminamos el dispositivo del carrito
      this.removeFromCart(device);
    } else {
      // De lo contrario, actualizamos la cantidad
      const index = this.cart.findIndex(d => d.id === device.id);
      if (index !== -1) {
        this.cart[index].selectedQuantity = newQuantity;
      }
    }
  }

  onQuantityChange(device: Device, newQuantity: number) {
    device.selectedQuantity = newQuantity;
  }

  /// Abre el modal
  openModal() {
    this.isModalOpen = true;
  }

  // Cierra el modal
  closeModal() {
    this.isModalOpen = false;
  }

  // Ver el contenido del carrito
  viewCart() {
    this.openModal();
  }

  // Enviar solicitud de préstamo
  async submitLoanRequest() {
    if (this.cart.length === 0) {
      const alert = await this.alertController.create({
        header: 'Error',
        message: 'Debes agregar al menos un dispositivo al carrito.',
        buttons: ['OK'],
      });
      await alert.present();
      return;
    }

    if (!this.loanPurpose || this.loanPurpose.trim() === '') {
      const alert = await this.alertController.create({
        header: 'Error',
        message: 'Por favor, proporciona un propósito para el préstamo.',
        buttons: ['OK'],
      });
      await alert.present();
      return;
    }

    // Construir el payload de la solicitud
    const requestPayload = {
      projectId: this.projectId, // Asegúrate de tener el projectId
      startDate: new Date().toISOString(), // Ajustar la fecha según sea necesario
      status: 'PENDING', // Estado inicial de la solicitud
      purpose: this.loanPurpose, // Valor dinámico del propósito ingresado
      loanRequestDevices: this.cart.map((device) => ({
        deviceId: device.id,
        quantity: device.selectedQuantity,
      })),
    };
    console.log(requestPayload);
    // Enviar la solicitud de préstamo
    this.loanRequestService.createLoanRequest(requestPayload).subscribe(
      async () => {
        const alert = await this.alertController.create({
          header: 'Éxito',
          message: 'Solicitud de préstamo enviada.',
          buttons: ['OK'],
        });
        await alert.present();
        this.closeModal(); // Cerrar el modal al completar el envío
      },
      async (error) => {
        console.error('Error submitting loan request:', error);
        const alert = await this.alertController.create({
          header: 'Error',
          message: 'Hubo un problema al enviar la solicitud.',
          buttons: ['OK'],
        });
        await alert.present();
      }
    );
  }

  // Mostrar alerta
  async presentAlert(message: string) {
    const alert = await this.alertController.create({
      header: 'Alerta',
      message,
      buttons: ['OK'],
    });
    await alert.present();
  }
}
