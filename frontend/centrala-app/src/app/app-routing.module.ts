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
import { OrdersComponent } from './homepage/orders/orders.component';
import { AuthorComponent } from './homepage/author/author.component';
import { AuthorTasksComponent } from './homepage/author/author-tasks/author-tasks.component';
import { TextSubbmitingComponent } from './homepage/author/text-subbmiting/text-subbmiting.component';
import { ChooseMagazineComponent } from './homepage/author/text-subbmiting/choose-magazine/choose-magazine.component';
import { SciencePaperFormComponent } from './homepage/author/text-subbmiting/science-paper-form/science-paper-form.component';
import { MembershipPaymentComponent } from './homepage/author/text-subbmiting/membership-payment/membership-payment.component';
import { CoauthorFormComponent } from './homepage/author/coauthor-form/coauthor-form.component';
import { ReviewPaperComponent } from './homepage/editor/review-paper/review-paper.component';
import { EditorPapersComponent } from './homepage/editor/editor-papers/editor-papers.component';
import { PaperFormatComponent } from './homepage/editor/paper-format/paper-format.component';
import { PaperCorrectionComponent } from './homepage/author/paper-correction/paper-correction.component';
import { ChooseReviwersComponent } from './homepage/editor/choose-reviwers/choose-reviwers.component';
import { ChiefEditorReviewComponent } from './homepage/editor/chief-editor-review/chief-editor-review.component';
import { ChiefEditorChoiceComponent } from './homepage/editor/chief-editor-choice/chief-editor-choice.component';

const appRoutes: Routes = [
	{ path: '', redirectTo: 'homepage/magazine-list', pathMatch: 'full'},
	{ path: 'homepage', component: HomepageComponent, children: [
		{ path: 'magazine-list', component: MagazineListComponent},
		{ path: 'orders', component: OrdersComponent},
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
			{ path: 'magazine-correction/:id', component: MagazineCorrectionComponent },
			{ path: 'papers', component: EditorPapersComponent },
			{ path: 'review-paper/:id', component: ReviewPaperComponent },
			{ path: 'paper-format/:processId', component: PaperFormatComponent },
			{ path: 'choose-reviwers/:id', component: ChooseReviwersComponent },
			{ path: 'chief-editor-review/:id', component: ChiefEditorReviewComponent },
			{ path: 'chief-editor-choice/:id', component: ChiefEditorChoiceComponent }
		]},
		{ path: 'author', component: AuthorComponent, children: [
			{ path: '', component: AuthorTasksComponent},
			{ path: 'text-subbmiting', component: TextSubbmitingComponent, children: [
				{path: '', component: ChooseMagazineComponent},
				{path: 'science-paper-form/:processId', component: SciencePaperFormComponent},
				{path: 'membership-payment/:processId', component: MembershipPaymentComponent},
				
			]},
			{ path: 'coauthor/:id', component: CoauthorFormComponent },
			{ path: 'paper-correction/:id', component: PaperCorrectionComponent }
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