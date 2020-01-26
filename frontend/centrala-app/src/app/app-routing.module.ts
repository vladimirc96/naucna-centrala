import { NgModule, Component } from '@angular/core';
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
import { MagazinesComponent } from './homepage/admin/magazines/magazines.component';
import { CheckMagazineDataComponent } from './homepage/admin/check-magazine-data/check-magazine-data.component';
import { EditorTasksComponent } from './homepage/editor/editor-tasks/editor-tasks.component';
import { MagazineCorrectionComponent } from './homepage/editor/magazine-correction/magazine-correction.component';
import { RegistrationFailureComponent } from './registration/registration-failure/registration-failure.component';
import { MagazineListComponent } from './homepage/magazine-list/magazine-list.component';
import { MagazineInfoComponent } from './homepage/magazine-info/magazine-info.component';

const appRoutes: Routes = [
	{ path: '', redirectTo: 'homepage/magazine-list', pathMatch: 'full'},
	{ path: 'homepage', component: HomepageComponent, children: [
		{ path: 'magazine-list', component: MagazineListComponent},
		{ path: 'magazine/:id', component: MagazineInfoComponent},
		{ path: 'admin', component: AdminComponent, children: [
			{ path: 'reviewer', component: ReviewerComponent },
			{ path: 'check-reviewer-data/:id', component: ReviewerFormComponent},
			{ path: 'magazines', component: MagazinesComponent},
			{ path: 'check-magazine-data/:id', component: CheckMagazineDataComponent}
		]},
		{ path: 'editor', component: EditorComponent, children: [
			{ path: '', component: EditorTasksComponent },
			{ path: 'magazines', component: MagazineFormComponent},
			{ path: 'editorial-board/:id', component: EditorialBoardFormComponent },
			{ path: 'magazine-correction/:id', component: MagazineCorrectionComponent }
		]}
	] },
	{ path: 'login', component: LoginComponent },
	{ path: 'registration', component: RegistrationComponent},
	{ path: 'registration-success', component: RegistrationSuccessComponent },
	{ path: 'registration-failure', component: RegistrationFailureComponent}
	
	]

@NgModule({
	imports: [RouterModule.forRoot(appRoutes)],
	exports: [ RouterModule ]
})



export class AppRoutingModule { }