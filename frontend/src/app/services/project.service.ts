import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  
  private baseUrl = `${environment.apiUrl}/projects`;

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('auth-token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  getAllProjects(): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}`, { headers });
  }

  createProject(projectData: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.baseUrl}`, projectData, { headers });
  }

  getProjectById(projectId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}/${projectId}`, { headers });
  }

  updateProject(projectId: number, projectData: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.put(`${this.baseUrl}/${projectId}`, projectData, { headers });
  }

  deleteProject(projectId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.delete(`${this.baseUrl}/${projectId}`, { headers });
  }

  getProjectMembers(projectId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}/${projectId}/members`, { headers });
  }

  getProjectWithMembers(projectId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}/${projectId}/with-members`, { headers });
  }

  getUserProjects(userId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}/user/${userId}`, { headers });
  }

  inviteMembers(projectId: number, userEmails: string[]): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.baseUrl}/${projectId}/invite`, { userEmails }, { headers });
  }

  acceptInvitation(token: string): Observable<any> {
    return this.http.post(`${this.baseUrl}/accept-invitation?token=${token}`, {});
  }

  getLoanRequests(projectId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}/${projectId}/loan-requests`, { headers });
  }
}
