import { Component, OnInit } from '@angular/core';
import { KPService } from 'src/app/services/kp.service';
import { ActivatedRoute, Params } from '@angular/router';

@Component({
  selector: 'app-magazine-plans',
  templateUrl: './magazine-plans.component.html',
  styleUrls: ['./magazine-plans.component.css']
})
export class MagazinePlansComponent implements OnInit {

  magazineId: any;
  billingPlans: any = null;
  pribavio: boolean = false;
  imaPlanova: boolean = false;

  constructor(private kpService: KPService, private route: ActivatedRoute) {
    this.route.params.subscribe(
      (params: Params) => {
        this.magazineId = params['id'];
      }
    );
    
  }

  ngOnInit() {
    this.kpService.getPlans(this.magazineId).subscribe(
      (response) => {
        this.pribavio = true;
        this.billingPlans = response;
        if(this.billingPlans.length != 0) {
          this.imaPlanova = true;
        }
      },
      (error) => {
        alert(error.message);
      }
    );
  }

}
