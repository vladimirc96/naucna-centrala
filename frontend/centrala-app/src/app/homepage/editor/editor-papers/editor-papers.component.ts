import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-editor-papers',
  templateUrl: './editor-papers.component.html',
  styleUrls: ['./editor-papers.component.css']
})
export class EditorPapersComponent implements OnInit {
  
  formFieldsDto = null;
  formFields = [];
  chooseReviewerTasks = [];
  paperReviewTasks = [];
  chiefEditorRevieweingTasks = [];
  chiefEditorChoiceTasks = [];

  constructor(private repoService: RepositoryService, private router: Router) { 
    this.repoService.getChooseReviewerTasks().subscribe(
      (response: any) => {
        this.chooseReviewerTasks = response;
      },
      (error) => { alert(error.message) }
    )

    this.repoService.getReviewPaperTasks().subscribe(
      (response: any) => {
        this.paperReviewTasks = response;
      },
      (error) => { alert(error.message) }
    )

    this.repoService.getChiefEditorReviewingTasks().subscribe(
      (response: any) => {
        this.chiefEditorRevieweingTasks = response;
      },
      (error) => { alert(error.message) }
    )

    this.repoService.getChiefEditorChoiceTasks().subscribe(
      (response: any) => {
        this.chiefEditorChoiceTasks = response;
      },
      (error) => { alert(error.message) }
    )

  }

  ngOnInit() {
  }

  claimPaperReviweTask(taskId){
    this.repoService.claimTask(taskId).subscribe(
      (success) => {
        this.router.navigate(['/homepage/editor/review-paper/' + taskId]);
      }
    )
  }

  claimChooseReviewerTask(taskId){
    this.repoService.claimTask(taskId).subscribe(
      (success) => {
        this.router.navigate(['/homepage/editor/choose-reviwers/'.concat(taskId)]);
      }
    )
  }

  claimChiefEditorReviewTask(taskId){
    this.repoService.claimTask(taskId).subscribe(
      (success) => {
        this.router.navigate(['/homepage/editor/chief-editor-review/'.concat(taskId)]);
      }
    )
  }

  claimChiefEditorChoiceTask(taskId){
    this.repoService.claimTask(taskId).subscribe(
      (success) => {
        this.router.navigate(['/homepage/editor/chief-editor-choice/'.concat(taskId)]);
      }
    )
  }

}
