import { Component, OnInit } from '@angular/core';
import { MagazineService } from 'src/app/services/magazine.service.';
import { ActivatedRoute, Params } from '@angular/router';
import { KPService } from 'src/app/services/kp.service';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-magazine-info',
  templateUrl: './magazine-info.component.html',
  styleUrls: ['./magazine-info.component.css']
})
export class MagazineInfoComponent implements OnInit {

  magazineId: any;
  magazine: any;
  retHref: any;


  constructor(private magazineService: MagazineService, private route: ActivatedRoute, private kpService: KPService, private orderService: OrderService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.magazineId = params['id'];
      }
    );


   }

  ngOnInit() {
    this.magazineService.get(this.magazineId).subscribe(
      (response) => {
        this.magazine = response;
      },
      (error) => {
        alert(error.message);
      }
    )
  }

  onPlan() {
    this.kpService.createPlan(this.magazineId).subscribe(
      (response) => {
        this.retHref = response;
        window.location.href = this.retHref.href;
      },
      (error) => {
        alert(error.message);
      }
    );

  }

  onKupi(){
    this.orderService.initMagazineOrder(this.magazine).subscribe(
      (response: any) => {
        window.open(response.redirectUrl, "_blank");
      },
      (error) => {
        alert(error.message);
      }
    )
  }

  onPretplatise() {
    this.kpService.subscriptions(this.magazineId).subscribe(
      (response) => {
        this.retHref = response;
        window.location.href = this.retHref.href;
      },
      (error) => {
        alert(error.message);
      }
    );
  }

}
