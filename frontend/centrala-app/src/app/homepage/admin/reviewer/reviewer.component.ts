import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';
import { Router } from '@angular/router';
import { AdminService } from 'src/app/services/admin.service';

@Component({
  selector: 'app-reviewer',
  templateUrl: './reviewer.component.html',
  styleUrls: ['./reviewer.component.css']
})
export class ReviewerComponent implements OnInit {

  tasks = [];

  constructor(private repoService: RepositoryService, private router: Router, private adminService: AdminService) { }

  ngOnInit() {
    this.adminService.getTasksReviewer().subscribe(
      (response: any) => {
        this.tasks = response;
      }
    ),
    (error) => {
      alert(error);
    }
  }

  claimTask(taskId){
    this.repoService.claimTask(taskId).subscribe(
      (success) => {
          this.router.navigate(['/homepage/admin/check-reviewer-data/' + taskId]);
      },
      (error) => {alert(error)}
    )
    
    
  }


}
