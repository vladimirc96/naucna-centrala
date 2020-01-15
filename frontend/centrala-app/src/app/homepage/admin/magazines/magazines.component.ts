import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/services/admin.service';
import { RepositoryService } from 'src/app/services/repository.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-magazines',
  templateUrl: './magazines.component.html',
  styleUrls: ['./magazines.component.css']
})
export class MagazinesComponent implements OnInit {

  tasks = [];

  constructor(private repoService: RepositoryService, private router: Router, private adminService: AdminService) { }

  ngOnInit() {

    this.adminService.getTasksCheckMagazineData().subscribe(
      (response: any)=>{
        this.tasks = response;
      }
    ),
    (error) => {
      alert(error.message);
    }
  }

  claimTask(taskId){
    this.repoService.claimTask(taskId).subscribe(
      (success) => {
        this.router.navigate(['/homepage/admin/check-magazine-data/' + taskId]);
      }
    )
  }

}
