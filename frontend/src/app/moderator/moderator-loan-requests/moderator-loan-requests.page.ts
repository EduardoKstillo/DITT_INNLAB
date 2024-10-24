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

  ionViewWillEnter() {
    this.loadLoanRequests();  // También recargar las invitaciones cuando entras a la página
  }

  loadLoanRequests(event?: any) {
    this.loanRequestService.getLoanRequests().subscribe(
      (response) => {
        this.loanRequests = response; // Cargar solicitudes de préstamo
        if (event) {
          event.target.complete(); // Finaliza el refresco
        }
      },
      (error) => {
        console.error('Error fetching loan requests:', error);
        if (event) {
          event.target.complete(); // Finaliza el refresco
        }
      }
    );
  }

  viewRequestDetails(loanRequestId: number) {
    this.router.navigate(['/loan-request-details', loanRequestId]); // Navegar a la página de detalles de la solicitud
  }
}
