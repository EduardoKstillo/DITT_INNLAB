import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AllProjectsPage } from './all-projects.page';

const routes: Routes = [
  {
    path: '',
    component: AllProjectsPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class AllProjectsPageRoutingModule {}
