import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../services/project.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { AlertController } from '@ionic/angular';

@Component({
  selector: 'app-my-projects',
  templateUrl: './my-projects.page.html',
  styleUrls: ['./my-projects.page.scss'],
})
export class MyProjectsPage implements OnInit {
  projects: any[] = [];
  loggedInUserName: string;

  constructor(
    private projectService: ProjectService,
    private authService: AuthService,
    private router: Router,
    private alertController: AlertController
  ) {
    this.loggedInUserName = this.authService.getLoggedInUser() ?? '';
  }

  ngOnInit() {
    this.loadUserProjects();
  }

  loadUserProjects() {
    const userId = this.authService.getLoggedInUserId();
    
    if (userId !== null) {
        this.projectService.getUserProjects(userId).subscribe(
            (response) => {
                this.projects = response;
            },
            (error) => {
                console.error('Error fetching user projects:', error);
            }
        );
    } else {
        console.error('User ID is null, cannot fetch projects');
    }
  }

  createProject() {
    this.router.navigate(['/create-project']); // Navegar a la página de creación de proyectos
  }

  viewProjectDetails(projectId: number) {
    this.router.navigate(['/project-details', projectId]); // Navegar a la página de detalles del proyecto
  }

  async deleteProject(projectId: number, event: Event) {
    event.stopPropagation(); // Prevenir la propagación del evento al ion-item
    const alert = await this.alertController.create({
      header: 'Confirmar',
      message: '¿Estás seguro de que deseas eliminar este proyecto?',
      buttons: [
        {
          text: 'Cancelar',
          role: 'cancel',
          cssClass: 'secondary',
        },
        {
          text: 'Eliminar',
          handler: () => {
            this.projectService.deleteProject(projectId).subscribe(
              () => {
                this.loadUserProjects(); // Recargar los proyectos después de eliminar
              },
              (error) => {
                console.error('Error deleting project:', error);
              }
            );
          },
        },
      ],
    });

    await alert.present();
  }

  editProject(projectId: number) {
    this.router.navigate(['/edit-project', projectId]); // Navegar a la página de edición de proyectos
  }
}
