import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { NotificationsPushService } from '../services/notifications-push.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.page.html',
  styleUrls: ['./login.page.scss'],
})
export class LoginPage {
  credentials = {
    email: '',
    password: ''
  };

  // Variables para la visibilidad de la contraseña
  passwordType: string = 'password';
  passwordIcon: string = 'eye-off'; // Icono para mostrar u ocultar la contraseña

  constructor(private authService: AuthService, private router: Router, private notificationsService: NotificationsPushService) {}

  // Método para alternar la visibilidad de la contraseña
  togglePasswordVisibility() {
    if (this.passwordType === 'password') {
      this.passwordType = 'text'; // Mostrar la contraseña
      this.passwordIcon = 'eye';  // Cambiar el icono a ojo abierto
    } else {
      this.passwordType = 'password'; // Ocultar la contraseña
      this.passwordIcon = 'eye-off';  // Cambiar el icono a ojo cerrado
    }
  }

  login() {
    if (this.credentials.email && this.credentials.password) {
      this.authService.login(this.credentials).subscribe(response => {
        console.log('Login successful', response);
        const deviceToken = this.notificationsService.getDeviceToken();

        if (deviceToken ){
          // Registra el token en el servidor
          this.authService.registerDeviceToken(deviceToken).subscribe(
            () => console.log('Token registered successfully'),
            error => console.error('Error registering token', error)
          );
        }

        this.router.navigate(['/home']);
      }, error => {
        console.error('Login failed', error);
      });
    }
  }
}
