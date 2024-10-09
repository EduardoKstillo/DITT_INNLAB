import { Component, OnInit } from '@angular/core';
import { InvitationService } from '../services/invitation.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.page.html',
  styleUrls: ['./notifications.page.scss'],
})
export class NotificationsPage implements OnInit {
  invitations: any[] = [];
  userId: number;

  constructor(private invitationService: InvitationService, private authService: AuthService) {
    this.userId = this.authService.getLoggedInUserId() ?? 0;
  }

  ngOnInit() {
    this.loadInvitations();
  }

  loadInvitations() {
    this.invitationService.getInvitationsByUser(this.userId).subscribe(
      (data) => {
        this.invitations = data;
      },
      (error) => {
        console.error('Error al cargar las invitaciones:', error);
      }
    );
  }

  // Aceptar invitación
  acceptInvitation(invitationId: number) {
    this.invitationService.acceptInvitation(invitationId).subscribe(
      () => {
        alert('Invitación aceptada');
        this.loadInvitations(); // Recargar las invitaciones
      },
      (error) => {
        console.error('Error al aceptar la invitación:', error);
      }
    );
  }

  // Rechazar invitación
  rejectInvitation(invitationId: number) {
    this.invitationService.rejectInvitation(invitationId).subscribe(
      () => {
        alert('Invitación rechazada');
        this.loadInvitations(); // Recargar las invitaciones
      },
      (error) => {
        console.error('Error al rechazar la invitación:', error);
      }
    );
  }
}
