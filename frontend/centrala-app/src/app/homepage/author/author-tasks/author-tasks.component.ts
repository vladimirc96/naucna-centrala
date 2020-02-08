import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-author-tasks',
  templateUrl: './author-tasks.component.html',
  styleUrls: ['./author-tasks.component.css']
})
export class AuthorTasksComponent implements OnInit {

  tasks: any = [];

  constructor(private repoService: RepositoryService, private router: Router) { }

  ngOnInit() {
    this.repoService.getAddCoauthorTasks().subscribe(
      (response: any) => {
        this.tasks = response;
      },
      (error) => {
        alert(error.message);
      }
    )
  }

  claimTask(taskId){
    this.repoService.claimTask(taskId).subscribe(
      (success) => {
        this.router.navigate(['/homepage/author/coauthor/'.concat(taskId)]);
      }
    ),
    (error) => {
      alert(error.message);
    }
  }

}
