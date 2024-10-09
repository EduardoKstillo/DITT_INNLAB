import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { InviteMembersPageRoutingModule } from './invite-members-routing.module';

import { InviteMembersPage } from './invite-members.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    InviteMembersPageRoutingModule
  ],
  declarations: [InviteMembersPage]
})
export class InviteMembersPageModule {}
