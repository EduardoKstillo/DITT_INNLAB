import { Component, OnInit } from '@angular/core';
import { LoanRequestService } from '../../services/loan-request.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-moderator-loan-requests',
  templateUrl: './moderator-loan-requests.page.html',
  styleUrls: ['./moderator-loan-requests.page.scss'],
})
export class ModeratorLoanRequestsPage implements OnInit {
  loanRequests: any[] = []; // Lista de solicitudes de préstamo

  constructor(
    private loanRequestService: LoanRequestService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadLoanRequests();
  }

  loadLoanRequests() {
    this.loanRequestService.getLoanRequests().subscribe(
      (response) => {
        this.loanRequests = response; // Cargar solicitudes de préstamo
      },
      (error) => {
        console.error('Error fetching loan requests:', error);
      }
    );
  }

  viewRequestDetails(loanRequestId: number) {
    this.router.navigate(['/loan-request-details', loanRequestId]); // Navegar a la página de detalles de la solicitud
  }
}
