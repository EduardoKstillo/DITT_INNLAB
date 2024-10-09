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

  constructor(
    private authService: AuthService,
    private menu: MenuController,
    private userService: UserService,
    private router: Router,
    private alertController: AlertController
  ) {}

  ngOnInit() {
    this.loadUserProfile();
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
}
