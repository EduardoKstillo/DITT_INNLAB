import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { ModeratorLoanRequestsPage } from './moderator-loan-requests.page';

const routes: Routes = [
  {
    path: '',
    component: ModeratorLoanRequestsPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ModeratorLoanRequestsPageRoutingModule {}
