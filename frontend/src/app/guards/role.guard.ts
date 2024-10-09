import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { RouteData } from '../interfaces/route-data.interface';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const user = this.authService.getUser();

    // Verificar si el usuario está autenticado
    if (!user) {
      this.router.navigate(['/login']); // Redirigir a login si no está autenticado
      return false;
    }

    const roles = user.roles || [];
    const expectedRoles = (route.data as RouteData).expectedRoles; // Acceso a expectedRoles con la interfaz

    // Verificar si el usuario tiene al menos uno de los roles requeridos
    const hasAccess = expectedRoles && expectedRoles.some(role => roles.includes(role));

    if (hasAccess) {
      return true; // Permitir acceso
    }

    // Si no tiene permiso, redirigir a una página no autorizada
    this.router.navigate(['/unauthorized']);
    return false; // No permitir acceso
  }
}
