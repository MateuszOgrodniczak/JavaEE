import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {UsersComponent} from './users/users.component';
import {ItemDetailComponent} from './item-detail/item-detail.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {BuildingsComponent} from './buildings/buildings.component';
import {ApplicationsComponent} from './applications/applications.component';
import {StatisticsComponent} from './statistics/statistics.component';
import {EmailComponent} from "./email/email.component";
import {LoginComponent} from "./login/login.component";

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'users', component: UsersComponent },
  { path: 'buildings', component: BuildingsComponent },
  { path: 'applications', component: ApplicationsComponent },
  { path: 'detail/user/:id', component: ItemDetailComponent},
  { path: 'detail/building/:id', component: ItemDetailComponent},
  { path: 'detail/application/:id', component: ItemDetailComponent},
  { path: 'statistics', component: StatisticsComponent },
  { path: 'email', component: EmailComponent },
  { path: 'login', component: LoginComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
