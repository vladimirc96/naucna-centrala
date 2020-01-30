import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';
import { Router } from '@angular/router';
import { MagazineService } from 'src/app/services/magazine.service.';
import { KPService } from 'src/app/services/kp.service';

@Component({
  selector: 'app-editor-tasks',
  templateUrl: './editor-tasks.component.html',
  styleUrls: ['./editor-tasks.component.css']
})
export class EditorTasksComponent implements OnInit {

  tasks = [];
  magazineList: any = [];
  emptyMagazineList: boolean = false;
  retHref: any;

  constructor(private repoService: RepositoryService, private magazineService: MagazineService,private router: Router, private kpService: KPService) { }

  ngOnInit() {
    this.magazineService.getMagazineCorrectionTasks().subscribe(
      (response: any) => {
        this.tasks = response;
      }
    ),
    (error) => {
      alert(error.message);
    }

    this.magazineService.getAllByChiefEditor().subscribe(
      (data) => {
        this.magazineList = data;
      }, 
      (error) => {
        alert(error.message);
      }
    )

  }

  claimTask(taskId){
    this.repoService.claimTask(taskId).subscribe(
      (success) => {
        this.router.navigate(['/homepage/editor/magazine-correction/' + taskId]);
      }
    )
  }

  onRegisterMagazine(id) {

    let dto = {
      id: id
    }

    this.kpService.registerMagazinSeller(dto).subscribe(
      (res: any) => {
        this.magazineList = this.magazineList.map(m => {
          if (m.id == dto.id) {
            m.sellerId = "x";
          }

          return m;
        })
        window.location.href = res.registrationPageRedirectUrl;
      }, err=> console.log(err.error)
    )
  }

  onReviewRegistration(id) {
    
    let dto = {
      id: id
    }

    this.kpService.reviewRegistration(dto).subscribe(
      (res: any) => {
        window.location.href = res.registrationPageRedirectUrl;
      }, err => console.log(err.error)
    )
    
  }

  onPlan(magazineID) {
    this.kpService.createPlan(magazineID).subscribe(
      (response) => {
        this.retHref = response;
        if(this.retHref.href === "noPP") {
          alert("Nije moguce napraviti plan jer PayPal nije registrovan!");
        } else {
          window.location.href = this.retHref.href;
        }
      },
      (error) => {
        alert(error.message);
      }
    );
  }

  onListPlans(magazineID) {
    window.location.href = "http://localhost:4201/billingplans/show/" + magazineID;
  }

}
