import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.page.html',
  styleUrls: ['./signup.page.scss'],
})
export class SignupPage {
  user = {
    firstName: '',
    lastName: '',
    email: '',
    university: '',
    dni: '',
    password: ''
  };
  
  confirmPassword: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  register() {
    if (this.user.password === this.confirmPassword) {
      // Añadir el rol predeterminado "user"
      const userData = {
        ...this.user,
        role: ['user'] // Rol predeterminado
      };
      console.log(userData);
      // Llamar al servicio de autenticación para registrar al usuario
      this.authService.signUp(userData).subscribe(response => {
        console.log('Registro exitoso', response);
        this.router.navigate(['/login']);
      }, error => {
        console.error('Error al registrar', error);
      });
    } else {
      console.error('Las contraseñas no coinciden');
    }
  }
}
