import { Component, OnInit } from '@angular/core';
import { InvitationService } from '../services/invitation.service';
import { AuthService } from '../services/auth.service';
import { AlertController } from '@ionic/angular';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.page.html',
  styleUrls: ['./notifications.page.scss'],
})
export class NotificationsPage implements OnInit {
  invitations: any[] = [];
  userId: number;

  constructor(
    private invitationService: InvitationService, 
    private authService: AuthService,
    private alertController: AlertController
  ) {
    this.userId = this.authService.getLoggedInUserId() ?? 0;
  }

  ngOnInit() {
    this.loadInvitations();
  }

  ionViewWillEnter() {
    this.loadInvitations();  // También recargar las invitaciones cuando entras a la página
  }

  loadInvitations(event?: any) {
    this.invitationService.getInvitationsByUser(this.userId).subscribe(
      (data) => {
        this.invitations = data;
        if (event) {
          event.target.complete(); // Finaliza el refresco
        }
      },
      (error) => {
        console.error('Error al cargar las invitaciones:', error);
        if (event) {
          event.target.complete(); // Finaliza el refresco incluso en caso de error
        }
      }
    );
  }

  // Aceptar invitación con alerta de confirmación
  async acceptInvitation(invitationId: number) {
    const alert = await this.alertController.create({
      header: 'Confirmar',
      message: '¿Estás seguro de que deseas aceptar esta invitación?',
      buttons: [
        {
          text: 'Cancelar',
          role: 'cancel',
          cssClass: 'secondary',
        },
        {
          text: 'Aceptar',
          handler: () => {
            this.invitationService.acceptInvitation(invitationId).subscribe(
              async () => {
                const successAlert = await this.alertController.create({
                  header: 'Éxito',
                  message: 'Invitación aceptada.',
                  buttons: ['OK'],
                });
                await successAlert.present();
                this.loadInvitations(); // Recargar las invitaciones
              },
              async (error) => {
                console.error('Error al aceptar la invitación:', error);
                const errorAlert = await this.alertController.create({
                  header: 'Error',
                  message: 'Error al aceptar la invitación.',
                  buttons: ['OK'],
                });
                await errorAlert.present();
              }
            );
          },
        },
      ],
    });

    await alert.present();
  }

  // Rechazar invitación con alerta de confirmación
  async rejectInvitation(invitationId: number) {
    const alert = await this.alertController.create({
      header: 'Confirmar',
      message: '¿Estás seguro de que deseas rechazar esta invitación?',
      buttons: [
        {
          text: 'Cancelar',
          role: 'cancel',
          cssClass: 'secondary',
        },
        {
          text: 'Rechazar',
          handler: () => {
            this.invitationService.rejectInvitation(invitationId).subscribe(
              async () => {
                const successAlert = await this.alertController.create({
                  header: 'Éxito',
                  message: 'Invitación rechazada.',
                  buttons: ['OK'],
                });
                await successAlert.present();
                this.loadInvitations(); // Recargar las invitaciones
              },
              async (error) => {
                console.error('Error al rechazar la invitación:', error);
                const errorAlert = await this.alertController.create({
                  header: 'Error',
                  message: 'Error al rechazar la invitación.',
                  buttons: ['OK'],
                });
                await errorAlert.present();
              }
            );
          },
        },
      ],
    });

    await alert.present();
  }
}
