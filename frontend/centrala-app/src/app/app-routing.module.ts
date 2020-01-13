import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrationComponent } from './registration/registration.component';
import { RegistrationSuccessComponent } from './registration/registration-success/registration-success.component';
import { HomepageComponent } from './homepage/homepage.component';
import { LoginComponent } from './login/login.component';
import { AdminComponent } from './admin/admin.component';
import { ReviewerComponent } from './admin/reviewer/reviewer.component';

const appRoutes: Routes = [
	{ path: '', redirectTo: 'homepage', pathMatch: 'full'},
	{ path: 'homepage', component: HomepageComponent },
	{ path: 'login', component: LoginComponent },
	{ path: 'registration', component: RegistrationComponent},
	{ path: 'registration-success', component: RegistrationSuccessComponent },
	{ path: 'admin', component: AdminComponent, children: [
		{ path: 'reviewer', component: ReviewerComponent }
	]},
	]

@NgModule({
	imports: [RouterModule.forRoot(appRoutes)],
	exports: [ RouterModule ]
})



export class AppRoutingModule { }