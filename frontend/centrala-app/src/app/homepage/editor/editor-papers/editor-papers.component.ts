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
  tasks = [];

  constructor(private repoService: RepositoryService, private router: Router) { 
    this.repoService.getReviewPaperTasks().subscribe(
      (response: any) => {
        this.tasks = response;
      },
      (error) => {
        alert(error.message);
      }
    )
  }

  ngOnInit() {
  }

  claimTask(taskId){
    this.repoService.claimTask(taskId).subscribe(
      (success) => {
        this.router.navigate(['/homepage/editor/review-paper/' + taskId]);
      }
    )
  }


}
