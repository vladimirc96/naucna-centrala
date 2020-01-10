import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrationComponent } from './registration/registration.component';
import { RegistrationSuccessComponent } from './registration/registration-success/registration-success.component';

const appRoutes: Routes = [
	{ path: '', redirectTo: 'registration', pathMatch: 'full'},
	{ path: 'registration', component: RegistrationComponent},
	{ path: 'registration-success', component: RegistrationSuccessComponent }
]

@NgModule({
	imports: [RouterModule.forRoot(appRoutes)],
	exports: [ RouterModule ]
})



export class AppRoutingModule { }