import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { LoanRequestDetailsPageRoutingModule } from './loan-request-details-routing.module';

import { LoanRequestDetailsPage } from './loan-request-details.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    LoanRequestDetailsPageRoutingModule
  ],
  declarations: [LoanRequestDetailsPage]
})
export class LoanRequestDetailsPageModule {}
