import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class InvitationService {
  private baseUrl = `${environment.apiUrl}/invitations`;

  constructor(private http: HttpClient) {}

  // Obtener headers con el token de autenticación
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('auth-token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  // Obtener todas las invitaciones del usuario autenticado
  getInvitationsByUser(userId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}/user/${userId}`, {headers});
  }

  // Aceptar una invitación (modificado para enviar como URL param)
acceptInvitation(invitationId: number): Observable<any> {
  const headers = this.getAuthHeaders();
  return this.http.post(`${this.baseUrl}/${invitationId}/accept`, {}, { headers });
}

// Rechazar una invitación (modificado para enviar como URL param)
rejectInvitation(invitationId: number): Observable<any> {
  const headers = this.getAuthHeaders();
  return this.http.post(`${this.baseUrl}/${invitationId}/reject`, {}, { headers });
}
  
  // Invitar miembros al proyecto
  inviteMembers(projectId: number, emails: string[], inviterId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    const body = { emails, inviterId };  // Ahora incluye el ID del usuario que invita
    return this.http.post(`${this.baseUrl}/project/${projectId}/invite`, body, { headers });
  }
}
