import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';

@Component({
  selector: 'app-reviewer',
  templateUrl: './reviewer.component.html',
  styleUrls: ['./reviewer.component.css']
})
export class ReviewerComponent implements OnInit {

  tasks = [];

  constructor(private repoService: RepositoryService) { }

  ngOnInit() {
    this.repoService.getTasksReviewer().subscribe(
      (response: any) => {
        this.tasks = response;
      }
    ),
    (error) => {
      alert(error);
    }
  }

 
}
