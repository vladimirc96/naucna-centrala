import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrationComponent } from './registration/registration.component';

const appRoutes: Routes = [
	{ path: '', redirectTo: 'registration', pathMatch: 'full'},
	{ path: 'registration', component: RegistrationComponent},
]

@NgModule({
	imports: [RouterModule.forRoot(appRoutes)],
	exports: [ RouterModule ]
})



export class AppRoutingModule { }