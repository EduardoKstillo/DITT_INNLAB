import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';
import { RoleGuard } from './guards/role.guard';
import { UnauthorizedComponent } from './unauthorized/unauthorized.component';
import { AuthGuard } from './guards/auth.guard';

const routes: Routes = [
  {
    path: 'home',
    loadChildren: () => import('./home/home.module').then( m => m.HomePageModule),
    // canActivate: [RoleGuard],
    // data: { expectedRoles: ['ROLE_USER', 'ROLE_MODERATOR', 'ROLE_ADMIN'] }
  },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadChildren: () => import('./login/login.module').then( m => m.LoginPageModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'signup',
    loadChildren: () => import('./signup/signup.module').then( m => m.SignupPageModule),
    canActivate: [AuthGuard]
  },
  {
    path: 'my-projects',
    loadChildren: () => import('./user/my-projects/my-projects.module').then( m => m.MyProjectsPageModule),
    // canActivate: [RoleGuard],
    // data: { expectedRoles: ['ROLE_USER', 'ROLE_MODERATOR'] }
  },
  {
    path: 'create-project',
    loadChildren: () => import('./user/create-project/create-project.module').then( m => m.CreateProjectPageModule),
    // canActivate: [RoleGuard],
    // data: { expectedRoles: ['ROLE_USER'] }
  },
  {
    path: 'project-details/:id',
    loadChildren: () => import('./user/project-details/project-details.module').then( m => m.ProjectDetailsPageModule),
    // canActivate: [RoleGuard],
    // data: { expectedRoles: ['ROLE_USER', 'ROLE_MODERATOR', 'ROLE_ADMIN'] }
  },
  {
    path: 'invite-members',
    loadChildren: () => import('./user/invite-members/invite-members.module').then( m => m.InviteMembersPageModule),
    // canActivate: [RoleGuard],
    // data: { expectedRoles: ['ROLE_USER'] }
  },
  {
    path: 'all-projects',
    loadChildren: () => import('./moderator/all-projects/all-projects.module').then( m => m.AllProjectsPageModule),
    // canActivate: [RoleGuard],
    // data: { expectedRoles: ['ROLE_MODERATOR', 'ROLE_ADMIN'] },
  },
  {
    path: 'request-loan/:projectId', // create a new request
    loadChildren: () => import('./request-loan/request-loan.module').then( m => m.RequestLoanPageModule),
    // canActivate: [RoleGuard],
    // data: { expectedRoles: ['ROLE_USER'] }
  },
  {
    path: 'moderator-loan-requests',
    loadChildren: () => import('./moderator/moderator-loan-requests/moderator-loan-requests.module').then( m => m.ModeratorLoanRequestsPageModule),
    // canActivate: [RoleGuard],
    // data: { expectedRoles: ['ROLE_MODERATOR', 'ROLE_ADMIN'] }
  },
  {
    path: 'loan-request-details/:id',
    loadChildren: () => import('./moderator/loan-request-details/loan-request-details.module').then( m => m.LoanRequestDetailsPageModule),
    // canActivate: [RoleGuard],
    // data: { expectedRoles: ['ROLE_MODERATOR', 'ROLE_ADMIN'] }
  },
  {
    path: 'device-list',
    loadChildren: () => import('./admin/device-list/device-list.module').then( m => m.DeviceListPageModule),
    // canActivate: [RoleGuard],
    // data: { expectedRoles: ['ROLE_MODERATOR', 'ROLE_ADMIN'] }
  },
  {
    path: 'device-create',
    loadChildren: () => import('./admin/device-create/device-create.module').then( m => m.DeviceCreatePageModule),
    // canActivate: [RoleGuard],
    // data: { expectedRoles: ['ROLE_MODERATOR', 'ROLE_ADMIN'] }
  },
  {
    path: 'device-detail/:id',
    loadChildren: () => import('./admin/device-detail/device-detail.module').then( m => m.DeviceDetailPageModule),
    // canActivate: [RoleGuard],
    // data: { expectedRoles: ['ROLE_MODERATOR', 'ROLE_ADMIN'] }
  },
  {
    path: 'device-update/:id',
    loadChildren: () => import('./admin/device-update/device-update.module').then( m => m.DeviceUpdatePageModule),
    canActivate: [RoleGuard],
    // data: { expectedRoles: ['ROLE_MODERATOR', 'ROLE_ADMIN'] }
  },
  {
    path: 'profile',
    loadChildren: () => import('./user/profile/profile.module').then( m => m.ProfilePageModule),
    // canActivate: [RoleGuard],
    // data: { expectedRoles: ['ROLE_USER', 'ROLE_MODERATOR', 'ROLE_ADMIN'] }
  },
  {
    path: 'unauthorized',
    component: UnauthorizedComponent
  },
  {
    path: 'request-loan',
    loadChildren: () => import('./request-loan/request-loan.module').then( m => m.RequestLoanPageModule)
  },
  {
    path: 'user-list',
    loadChildren: () => import('./admin/user-list/user-list.module').then( m => m.UserListPageModule)
  },
  {
    path: 'create-user',
    loadChildren: () => import('./admin/create-user/create-user.module').then( m => m.CreateUserPageModule)
  },
  {
    path: 'edit-user/:id',
    loadChildren: () => import('./admin/edit-user/edit-user.module').then( m => m.EditUserPageModule)
  },
  {
    path: 'notifications',
    loadChildren: () => import('./notifications/notifications.module').then( m => m.NotificationsPageModule)
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
