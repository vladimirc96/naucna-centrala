import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { RegistrationComponent } from './registration/registration.component';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegistrationSuccessComponent } from './registration/registration-success/registration-success.component';
import { HomepageComponent } from './homepage/homepage.component';
import { LoginComponent } from './login/login.component';
import { JwtInterceptor } from './_helpers/jwt.interceptor';
import { AdminComponent } from './admin/admin.component';
import { ReviewerComponent } from './admin/reviewer/reviewer.component';
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { ReviewerFormComponent } from './admin/reviewer-form/reviewer-form.component';
@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    RegistrationSuccessComponent,
    HomepageComponent,
    LoginComponent,
    AdminComponent,
    ReviewerComponent,
    ReviewerFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AngularFontAwesomeModule
  ],
  providers: [ { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }, ],
  bootstrap: [AppComponent]
})
export class AppModule { }
