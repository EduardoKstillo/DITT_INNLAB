import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { AllProjectsPageRoutingModule } from './all-projects-routing.module';

import { AllProjectsPage } from './all-projects.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    AllProjectsPageRoutingModule
  ],
  declarations: [AllProjectsPage]
})
export class AllProjectsPageModule {}
