import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UsersComponent } from './users/users.component';
import { ItemDetailComponent } from './item-detail/item-detail.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import {HttpClientModule} from '@angular/common/http';
import { BuildingsComponent } from './buildings/buildings.component';
import { ApplicationsComponent } from './applications/applications.component';
import { StatisticsComponent } from './statistics/statistics.component';
import { EmailComponent } from './email/email.component';
import {FormsModule} from "@angular/forms";

import { ToastrModule } from 'ngx-toastr';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { LoginComponent } from './login/login.component';
import { CookieService } from 'ngx-cookie-service';

@NgModule({
  declarations: [
    AppComponent,
    UsersComponent,
    ItemDetailComponent,
    DashboardComponent,
    BuildingsComponent,
    ApplicationsComponent,
    StatisticsComponent,
    EmailComponent,
    LoginComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
  ],
  providers: [CookieService],
  bootstrap: [AppComponent]
})
export class AppModule { }
