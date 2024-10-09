import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class LoanRequestService {
  private baseUrl = `${environment.apiUrl}/loan-requests`;

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('auth-token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  createLoanRequest(loanRequestData: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(`${this.baseUrl}`, loanRequestData, { headers })
      .pipe(
        catchError(error => {
          console.error('Error creating loan request:', error);
          return throwError(error);
        })
      );
  }

  getLoanRequests(): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}`, { headers });
  }

  getLoanRequestById(loanRequestId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}/${loanRequestId}`, { headers });
  }

  getLoanRequestsByStatus(status: string): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}/status/${status}`, { headers });
  }

  approveLoanRequest(loanRequestId: number, moderatorId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.patch<void>(`${this.baseUrl}/${loanRequestId}/approve?moderatorId=${moderatorId}`, {}, { headers });
  }

  rejectLoanRequest(loanRequestId: number, moderatorId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.patch<void>(`${this.baseUrl}/${loanRequestId}/reject?moderatorId=${moderatorId}`, {}, { headers });
  }

  returnLoanRequest(loanRequestId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.patch<void>(`${this.baseUrl}/${loanRequestId}/return`, {}, { headers });
  }

  approveReturn(loanRequestId: number, moderatorId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.patch<void>(`${this.baseUrl}/${loanRequestId}/approve-return?moderatorId=${moderatorId}`, {}, { headers });
  }

  cancelLoanRequest(loanRequestId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.patch<string>(`${this.baseUrl}/${loanRequestId}/cancel`, {}, { headers });
  }

  getLoanRequestsByProject(projectId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.get(`${this.baseUrl}/project/${projectId}`, { headers });
  }
}
