import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule  } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { RequestLoanPageRoutingModule } from './request-loan-routing.module';

import { RequestLoanPage } from './request-loan.page';
import { DeviceCounterComponent } from '../components/device-counter/device-counter.component';
import { CardModalComponent } from '../components/card-modal/card-modal.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    IonicModule,
    RequestLoanPageRoutingModule
  ],
  declarations: [RequestLoanPage, DeviceCounterComponent, CardModalComponent]
})
export class RequestLoanPageModule {}
