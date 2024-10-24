import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../services/project.service';
import { AlertController } from '@ionic/angular';
import { Router } from '@angular/router';

@Component({
  selector: 'app-all-projects',
  templateUrl: './all-projects.page.html',
  styleUrls: ['./all-projects.page.scss'],
})
export class AllProjectsPage implements OnInit {
  projects: any[] = []; // Lista de todos los proyectos

  constructor(
    private projectService: ProjectService,
    private alertController: AlertController,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadAllProjects();

    // Escuchar cuando se cree un nuevo proyecto
    this.projectService.projectCreated$.subscribe((created) => {
      if (created) {
        console.log("recargaaaaaa")
        this.loadAllProjects();
      }
    });
  }

  ionViewWillEnter() {
    this.loadAllProjects();  // También recargar los dispositivos cuando entras a la página
  }

  loadAllProjects(event?: any) {
    this.projectService.getAllProjects().subscribe(
      (response) => {
        this.projects = response; // Cargar proyectos
        if (event) {
          event.target.complete(); // Termina el refresco si existe
        }
      },
      (error) => {
        console.error('Error fetching projects:', error);
        if (event) {
          event.target.complete(); // Termina el refresco incluso en caso de error
        }
      }
    );
  }

  viewProjectDetails(projectId: number) {
    this.router.navigate(['/project-details', projectId]); // Navegar a la página de detalles del proyecto
  }
}
