import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../services/project.service';
import { ActivatedRoute, Router } from '@angular/router';
import { AlertController, ModalController } from '@ionic/angular';
import { InviteMembersPage } from '../invite-members/invite-members.page';
import { LoanRequestService } from '../../services/loan-request.service';
import { RequestLoanPage } from 'src/app/request-loan/request-loan.page';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-project-details',
  templateUrl: './project-details.page.html',
  styleUrls: ['./project-details.page.scss'],
})
export class ProjectDetailsPage implements OnInit {
  project: any; // Datos del proyecto
  loanRequests: any[] = []; // Solicitudes de préstamo
  isRequestModalOpen = false; // Controla si el modal está abierto
  loggedInUserName: string;

  constructor(
    private projectService: ProjectService,
    private loanRequestService: LoanRequestService,
    private route: ActivatedRoute,
    private alertController: AlertController,
    private modalController: ModalController,
    private router: Router,
    private authService: AuthService
  ) {
    this.loggedInUserName = this.authService.getLoggedInUser() ?? '';
  }

  ngOnInit() {
    
    const projectIdParam = this.route.snapshot.paramMap.get('id');
    const projectId = projectIdParam ? +projectIdParam : null;

    if (projectId !== null) {
      this.loadProjectDetails(projectId);
      this.loadLoanRequests(projectId);
    } else {
      console.error('Project ID is null');
    }
  }

  loadProjectDetails(projectId: number) {
    this.projectService.getProjectWithMembers(projectId).subscribe(
        (response) => {
            this.project = response;
        },
        async (error) => {
            console.error('Error fetching project details:', error);
            const alert = await this.alertController.create({
                header: 'Error',
                message: 'No se pudo cargar los detalles del proyecto.',
                buttons: ['OK'],
            });
            await alert.present();
        }
    );
  }

  // Método para cargar las solicitudes de préstamo del proyecto
  loadLoanRequests(projectId: number) {
    this.loanRequestService.getLoanRequestsByProject(projectId).subscribe(
      (response) => {
        this.loanRequests = response;
      },
      async (error) => {
        console.error('Error fetching loan requests:', error);
        const alert = await this.alertController.create({
          header: 'Error',
          message: 'No se pudieron cargar las solicitudes de préstamo.',
          buttons: ['OK'],
        });
        await alert.present();
      }
    );
  }

  async returnDevice(requestId: number, event: Event) {
    event.stopPropagation(); // Prevenir la propagación del evento si es necesario
    
    const alert = await this.alertController.create({
      header: 'Confirmar',
      message: '¿Estás seguro de que deseas devolver estos dispositivos?',
      buttons: [
        {
          text: 'Cancelar',
          role: 'cancel',
          cssClass: 'secondary',
        },
        {
          text: 'Devolver',
          handler: () => {
            // Llamar al servicio para devolver el dispositivo
            this.loanRequestService.returnLoanRequest(requestId).subscribe(
              async () => {
                const successAlert = await this.alertController.create({
                  header: 'Éxito',
                  message: 'Solicitud de devolución procesada.',
                  buttons: ['OK'],
                });
                await successAlert.present();
                // Recargar la lista de solicitudes o actualizar la vista
                this.loadLoanRequests(this.project.id);
              },
              async (error) => {
                console.error('Error returning loan request:', error);
                const errorAlert = await this.alertController.create({
                  header: 'Error',
                  message: 'Error al procesar la devolución de la solicitud.',
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
  

  async openInviteModal() {
    const modal = await this.modalController.create({
      component: InviteMembersPage,
      componentProps: { projectId: this.project.id } // Pasar el ID del proyecto
    });
    return await modal.present();
  }

  async goToLoanRequestPage() {
    // Navegar a la página de solicitud de préstamo, pasando el projectId
    this.router.navigate(['/request-loan', { projectId: this.project.id }]);
  }
}
