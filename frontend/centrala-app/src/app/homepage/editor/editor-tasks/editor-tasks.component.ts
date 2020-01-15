import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';
import { Router } from '@angular/router';
import { MagazineService } from 'src/app/services/magazine.service.';

@Component({
  selector: 'app-editor-tasks',
  templateUrl: './editor-tasks.component.html',
  styleUrls: ['./editor-tasks.component.css']
})
export class EditorTasksComponent implements OnInit {

  tasks = [];

  constructor(private repoService: RepositoryService, private magazineService: MagazineService,private router: Router) { }

  ngOnInit() {
    this.magazineService.getMagazineCorrectionTasks().subscribe(
      (response: any) => {
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
        this.router.navigate(['/homepage/editor/magazine-correction/' + taskId]);
      }
    )
  }

}
