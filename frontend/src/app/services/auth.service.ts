import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl = `${environment.apiUrl}/auth`;
  private TOKEN_KEY = 'auth-token';  // Nombre de la clave del token en el almacenamiento
  private USER_KEY = 'auth-user';  // Nombre de la clave del usuario en el almacenamiento

  constructor(private http: HttpClient) {}

  // Método para iniciar sesión
  login(credentials: { email: string, password: string }): Observable<any> {
    return this.http.post(`${this.baseUrl}/signin`, credentials).pipe(
      map((response: any) => {
        if (response.accessToken) {
          this.saveToken(response.accessToken);  // Guardar el token JWT en localStorage
          this.saveUser(response);  // Guardar los detalles del usuario en localStorage
        }
        return response;
      }),
      catchError(error => {
        console.error('Login error:', error);
        return throwError(error);
      })
    );
  }

  // Método para registrarse
  signUp(userData: {
    email: string;
    firstName: string;
    lastName: string;
    university: string;
    dni: string;
    password: string;
    role: string[];
  }): Observable<any> {
    return this.http.post(`${this.baseUrl}/signup`, userData).pipe(
      catchError(error => {
        console.error('Signup error:', error);
        return throwError(error);
      })
    );
  }

  // Metodo para registrar el dispositivo
  registerDeviceToken(token: string): Observable<any> {
    const userId = this.getLoggedInUserId(); // Obtener el ID del usuario autenticado
    return this.http.post(`${this.baseUrl}/register-device`, { userId, token }).pipe(
      catchError(error => {
        console.error('Error al registrar el token del dispositivo:', error);
        return throwError(error);
      })
    );
  }
  
  // Guardar el token JWT en localStorage
  saveToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);  // Guardar el token en el almacenamiento local
  }

  // Obtener el token JWT desde el almacenamiento
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);  // Obtener el token JWT desde localStorage
  }

  // Guardar los detalles del usuario en localStorage
  saveUser(user: any): void {
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));  // Guardar los detalles del usuario en formato JSON
  }

  // Obtener los detalles del usuario desde el almacenamiento
  getUser(): any {
    const user = localStorage.getItem(this.USER_KEY);  // Obtener el usuario desde localStorage
    return user ? JSON.parse(user) : null;  // Parsear el objeto de usuario si existe
  }

  // Obtener el nombre o email del usuario autenticado
  getLoggedInUser(): string | null {
    const user = this.getUser();
    return user ? user.username : null;  // Retorna el username (email) del usuario logueado
  }

  // Obtener el ID del usuario autenticado
  getLoggedInUserId(): number | null {
    const user = this.getUser();
    return user ? user.id : null;  // Retorna el ID del usuario autenticado si existe
  }

  // Logout - Eliminar el token y los detalles del usuario del almacenamiento
  logout(): void {
    const userId = this.getLoggedInUserId(); // Obtener el ID del usuario
    console.log("cerrar sesionj: ", userId)
    if (userId) {
        // Enviar solicitud para eliminar el token del dispositivo
        this.http.post(`${this.baseUrl}/logout-device/${userId}`, {}).subscribe(() => {
            console.log('Logout successful');
            localStorage.removeItem(this.TOKEN_KEY);  // Eliminar el token
            localStorage.removeItem(this.USER_KEY);   // Eliminar los detalles del usuario
        }, error => {
            console.error('Error during logout:', error);
        });
    }
}
  
  // Verificar si el usuario está autenticado (si hay un token almacenado)
  isAuthenticated(): boolean {
    return this.getToken() !== null;  // Si hay un token, el usuario está autenticado
  }

  // Obtener headers con el token JWT para peticiones autenticadas
  getAuthHeaders(): { headers: HttpHeaders } {
    const token = localStorage.getItem(this.TOKEN_KEY);
    return {
      headers: new HttpHeaders({
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      })
    };
  }
}
