import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    const isAuthenticated = this.authService.isAuthenticated();
    if (!isAuthenticated) {
      return true; // Permitir acceso a login y registro
    } else {
      this.router.navigate(['/home']); // Redirigir si ya está autenticado
      return false; // No permitir acceso a login y registro
    }
  }
}
