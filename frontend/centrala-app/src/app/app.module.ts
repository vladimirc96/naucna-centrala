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
import { MagazinesComponent } from './homepage/admin/magazines/magazines.component';
import { CheckMagazineDataComponent } from './homepage/admin/check-magazine-data/check-magazine-data.component';
import { EditorTasksComponent } from './homepage/editor/editor-tasks/editor-tasks.component';
import { MagazineCorrectionComponent } from './homepage/editor/magazine-correction/magazine-correction.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RegistrationFailureComponent } from './registration/registration-failure/registration-failure.component';
import { MagazineListComponent } from './homepage/magazine-list/magazine-list.component';
import { MagazineInfoComponent } from './homepage/magazine-info/magazine-info.component';
import { OrdersComponent } from './homepage/orders/orders.component';
import { AuthorComponent } from './homepage/author/author.component';
import { AuthorTasksComponent } from './homepage/author/author-tasks/author-tasks.component';
import { TextSubbmitingComponent } from './homepage/author/text-subbmiting/text-subbmiting.component';
import { ChooseMagazineComponent } from './homepage/author/text-subbmiting/choose-magazine/choose-magazine.component';
import { SciencePaperFormComponent } from './homepage/author/text-subbmiting/science-paper-form/science-paper-form.component';
import { MembershipPaymentComponent } from './homepage/author/text-subbmiting/membership-payment/membership-payment.component';
import { CoauthorFormComponent } from './homepage/author/coauthor-form/coauthor-form.component';
import { EditorPapersComponent } from './homepage/editor/editor-papers/editor-papers.component';
import { ReviewPaperComponent } from './homepage/editor/review-paper/review-paper.component';
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
    EditorialBoardFormComponent,
    MagazinesComponent,
    CheckMagazineDataComponent,
    EditorTasksComponent,
    MagazineCorrectionComponent,
    RegistrationFailureComponent,
    MagazineListComponent,
    MagazineInfoComponent,
    OrdersComponent,
    AuthorComponent,
    AuthorTasksComponent,
    TextSubbmitingComponent,
    ChooseMagazineComponent,
    SciencePaperFormComponent,
    MembershipPaymentComponent,
    CoauthorFormComponent,
    EditorPapersComponent,
    ReviewPaperComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    AngularFontAwesomeModule,
    NgxSpinnerModule,
    BrowserAnimationsModule
  ],
  providers: [ { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }, ],
  bootstrap: [AppComponent]
})
export class AppModule { }
