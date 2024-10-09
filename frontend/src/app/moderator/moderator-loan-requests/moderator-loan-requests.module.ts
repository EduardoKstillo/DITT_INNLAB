import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { ModeratorLoanRequestsPageRoutingModule } from './moderator-loan-requests-routing.module';

import { ModeratorLoanRequestsPage } from './moderator-loan-requests.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    ModeratorLoanRequestsPageRoutingModule
  ],
  declarations: [ModeratorLoanRequestsPage]
})
export class ModeratorLoanRequestsPageModule {}
