import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProjectService } from '../../services/project.service';
import { AlertController } from '@ionic/angular';

@Component({
  selector: 'app-edit-project',
  templateUrl: './edit-project.page.html',
  styleUrls: ['./edit-project.page.scss'],
})
export class EditProjectPage implements OnInit {
  projectId: number | null = null; // Inicializado como null
  projectData: any = {}; // Datos del proyecto

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService,
    private alertController: AlertController,
    private router: Router
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('projectId'); // Obtener el ID como string
    if (id) {
      this.projectId = +id; // Convertirlo a número
      this.loadProjectDetails(); // Cargar detalles del proyecto
    } else {
      console.error('Project ID is null or undefined. Redirecting to project list.');
      this.router.navigate(['/my-projects']); // Redirigir si no hay ID
    }
  }

  loadProjectDetails() {
    if (this.projectId) { // Verificar que projectId tiene un valor
      this.projectService.getProjectById(this.projectId).subscribe(
        (response) => {
          this.projectData = response; // Asignar la respuesta a la variable
          console.log(this.projectData)
        },
        (error) => {
          console.error('Error fetching project details:', error);
        }
      );
    }
  }

  async updateProject() {
    if (this.projectId) { // Verificar que projectId tiene un valor
      this.projectService.updateProject(this.projectId, this.projectData).subscribe(
        async () => {
          const alert = await this.alertController.create({
            header: 'Éxito',
            message: 'Proyecto actualizado con éxito.',
            buttons: ['OK']
          });
          await alert.present();
          this.router.navigate(['/my-projects']); // Regresar a la lista de proyectos
        },
        async (error) => {
          const alert = await this.alertController.create({
            header: 'Error',
            message: 'Error al actualizar el proyecto.',
            buttons: ['OK']
          });
          await alert.present();
        }
      );
    }
  }
}
