import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DeviceUpdatePage } from './device-update.page';

const routes: Routes = [
  {
    path: '',
    component: DeviceUpdatePage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DeviceUpdatePageRoutingModule {}
