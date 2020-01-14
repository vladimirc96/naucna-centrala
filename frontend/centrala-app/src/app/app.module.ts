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
import { AdminComponent } from './homepage/admin/admin.component';
import { ReviewerComponent } from './homepage/admin/reviewer/reviewer.component';
import { AngularFontAwesomeModule } from 'angular-font-awesome';
import { ReviewerFormComponent } from './homepage/admin/reviewer-form/reviewer-form.component';
import { EditorComponent } from './homepage/editor/editor.component';
import { NgxSpinnerModule } from "ngx-spinner";
import { MagazineFormComponent } from './homepage/editor/magazine-form/magazine-form.component';
import { EditorialBoardFormComponent } from './homepage/editor/editorial-board-form/editorial-board-form.component';

@NgModule({
  declarations: [
    AppComponent,
    RegistrationComponent,
    RegistrationSuccessComponent,
    HomepageComponent,
    LoginComponent,
    AdminComponent,
    ReviewerComponent,
    ReviewerFormComponent,
    EditorComponent,
    MagazineFormComponent,
    EditorialBoardFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AngularFontAwesomeModule,
    NgxSpinnerModule
  ],
  providers: [ { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }, ],
  bootstrap: [AppComponent]
})
export class AppModule { }
