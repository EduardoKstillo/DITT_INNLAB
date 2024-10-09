import { Component, Input } from '@angular/core';
import { ModalController } from '@ionic/angular';

@Component({
  selector: 'app-card-modal',
  templateUrl: './card-modal.component.html',
  styleUrls: ['./card-modal.component.scss'],
})
export class CardModalComponent {
  @Input() cart: any[] = []; // Asume que este array se llena con dispositivos añadidos al carrito.

  constructor(private modalController: ModalController) {}

  dismiss() {
    this.modalController.dismiss(); // Cierra el modal sin borrar el contenido de fondo
  }

  submitLoanRequest() {
    // Lógica para enviar la solicitud de préstamo
  }
}
