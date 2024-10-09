import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { AlertController } from '@ionic/angular';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.page.html',
  styleUrls: ['./profile.page.scss'],
})
export class ProfilePage implements OnInit {
  user: any = {};

  constructor(
    private authService: AuthService,
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
