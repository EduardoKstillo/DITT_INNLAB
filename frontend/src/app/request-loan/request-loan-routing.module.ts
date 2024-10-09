import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { RequestLoanPage } from './request-loan.page';

const routes: Routes = [
  {
    path: '',
    component: RequestLoanPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class RequestLoanPageRoutingModule {}
