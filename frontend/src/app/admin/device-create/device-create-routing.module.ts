import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DeviceCreatePage } from './device-create.page';

const routes: Routes = [
  {
    path: '',
    component: DeviceCreatePage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DeviceCreatePageRoutingModule {}
