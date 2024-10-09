import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { DeviceCreatePageRoutingModule } from './device-create-routing.module';

import { DeviceCreatePage } from './device-create.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    DeviceCreatePageRoutingModule
  ],
  declarations: [DeviceCreatePage]
})
export class DeviceCreatePageModule {}
