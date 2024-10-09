import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { InviteMembersPage } from './invite-members.page';

const routes: Routes = [
  {
    path: '',
    component: InviteMembersPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class InviteMembersPageRoutingModule {}
