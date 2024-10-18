import { Component } from '@angular/core';
import { AlertController, MenuController } from '@ionic/angular';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-side-menu',
  templateUrl: './side-menu.component.html',
  styleUrls: ['./side-menu.component.scss'],
})
export class SideMenuComponent {
  user: any = {};
  roles: string[] | null = null;
  isDarkMode: boolean = false;

  constructor(
    private authService: AuthService,
    private menu: MenuController,
    private userService: UserService,
    private router: Router,
    private alertController: AlertController
  ) {}

  ngOnInit() {
    this.roles = this.authService.getLoggedInUserRoles();
    this.loadUserProfile();
    this.initializeTheme();
  }

  loadUserProfile() {
    const userId = this.authService.getLoggedInUserId(); // Puede ser number o null
    
    // Verifica si userId no es nulo antes de llamar al servicio
    if (userId !== null) {
      this.userService.getUserById(userId).subscribe(
        (response) => {
          this.user = response; // Asignar la información del usuario
        },
        (error) => {
          console.error('Error fetching user profile:', error);
        }
      );
    } else {
      console.error('User ID is null, cannot fetch user profile');
      // Aquí puedes manejar el caso cuando el usuario no está autenticado
    }
  }


  closeMenu() {
    this.menu.close(); // Cierra el menú
  }

  async logout() {
    const alert = await this.alertController.create({
      header: 'Confirmar',
      message: '¿Estás seguro de que deseas cerrar sesión?',
      buttons: [
        {
          text: 'Cancelar',
          role: 'cancel',
        },
        {
          text: 'Cerrar Sesión',
          handler: () => {
            this.authService.logout(); // Cierra sesión
            this.router.navigate(['/login']); // Navegar a la página de inicio de sesión
          },
        },
      ],
    });

    await alert.present();
  }

  hasAdminRole(): boolean {
    return this.authService.hasRole('ROLE_ADMIN'); 
  }

  hasUserRole(): boolean {
    return this.authService.hasRole('ROLE_USER'); 
  }

  hasModRole(): boolean {
    return this.authService.hasRole('ROLE_MODERATOR'); 
  }

  initializeTheme() {
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    const savedTheme = localStorage.getItem('dark-theme');
    this.isDarkMode = savedTheme ? JSON.parse(savedTheme) : prefersDark;
    this.toggleDarkTheme(this.isDarkMode);
  }

  toggleTheme(event: any) {
    this.isDarkMode = event.detail.checked;
    this.toggleDarkTheme(this.isDarkMode);
    localStorage.setItem('dark-theme', JSON.stringify(this.isDarkMode));
  }

  toggleDarkTheme(shouldAdd: boolean) {
    document.body.classList.toggle('dark', shouldAdd);
  }
}
