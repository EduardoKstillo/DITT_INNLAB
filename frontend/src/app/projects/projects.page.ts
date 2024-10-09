import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../services/project.service';
import { AuthService } from '../services/auth.service'; // Servicio de autenticación para obtener el userId
import { Router } from '@angular/router';

@Component({
  selector: 'app-projects',
  templateUrl: './projects.page.html',
  styleUrls: ['./projects.page.scss'],
})
export class ProjectsPage implements OnInit {
  projects: any[] = [];
  userId: number | null = null; // Variable para almacenar el ID del usuario

  constructor(
    private projectService: ProjectService, 
    private authService: AuthService, // Inyectar el servicio de autenticación
    private router: Router
  ) {}

  ngOnInit() {
    this.loadUserProjects();
  }

  // Función para cargar los proyectos del usuario autenticado
  loadUserProjects() {
    // Obtener el ID del usuario autenticado desde el servicio de autenticación
    this.userId = this.authService.getLoggedInUserId();

    if (this.userId) {
      this.projectService.getUserProjects(this.userId).subscribe(
        (response) => {
          this.projects = response; // Almacenar los proyectos en la variable projects
        },
        (error) => {
          console.error('Error fetching user projects:', error);
        }
      );
    } else {
      console.error('User not authenticated');
      // Redirigir al login o mostrar un mensaje de error si el usuario no está autenticado
      this.router.navigate(['/login']);
    }
  }

  createProject() {
    this.router.navigate(['/create-project']);
  }

  viewProject(projectId: number) {
    this.router.navigate(['/project-details', projectId]);
  }
}
