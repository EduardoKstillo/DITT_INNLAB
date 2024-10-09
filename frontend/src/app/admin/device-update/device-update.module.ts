import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { DeviceUpdatePageRoutingModule } from './device-update-routing.module';

import { DeviceUpdatePage } from './device-update.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    DeviceUpdatePageRoutingModule
  ],
  declarations: [DeviceUpdatePage]
})
export class DeviceUpdatePageModule {}
