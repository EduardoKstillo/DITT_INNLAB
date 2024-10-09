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
  }

  loadAllProjects() {
    this.projectService.getAllProjects().subscribe(
      (response) => {
        this.projects = response; // Cargar proyectos
      },
      (error) => {
        console.error('Error fetching projects:', error);
      }
    );
  }

  viewProjectDetails(projectId: number) {
    this.router.navigate(['/project-details', projectId]); // Navegar a la página de detalles del proyecto
  }
}
