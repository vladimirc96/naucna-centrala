import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RegistrationComponent } from './registration/registration.component';
import { RegistrationSuccessComponent } from './registration/registration-success/registration-success.component';
import { HomepageComponent } from './homepage/homepage.component';
import { LoginComponent } from './login/login.component';
import { AdminComponent } from './homepage/admin/admin.component';
import { ReviewerComponent } from './homepage/admin/reviewer/reviewer.component';
import { ReviewerFormComponent } from './homepage/admin/reviewer-form/reviewer-form.component';
import { EditorComponent } from './homepage/editor/editor.component';
import { MagazineFormComponent } from './homepage/editor/magazine-form/magazine-form.component';
import { EditorialBoardFormComponent } from './homepage/editor/editorial-board-form/editorial-board-form.component';

const appRoutes: Routes = [
	{ path: '', redirectTo: 'homepage', pathMatch: 'full'},
	{ path: 'homepage', component: HomepageComponent, children: [
		{ path: 'admin', component: AdminComponent, children: [
			{ path: 'reviewer', component: ReviewerComponent },
			{ path: ':id', component: ReviewerFormComponent}
		]},
		{ path: 'editor', component: EditorComponent, children: [
			{path: 'magazines', component: MagazineFormComponent},
			{path: 'editorial-board/:id', component: EditorialBoardFormComponent}
		]}
	] },
	{ path: 'login', component: LoginComponent },
	{ path: 'registration', component: RegistrationComponent},
	{ path: 'registration-success', component: RegistrationSuccessComponent },
	
	]

@NgModule({
	imports: [RouterModule.forRoot(appRoutes)],
	exports: [ RouterModule ]
})



export class AppRoutingModule { }