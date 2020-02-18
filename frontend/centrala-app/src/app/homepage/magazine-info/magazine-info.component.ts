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
  pretplacen: boolean = false;
  ulogovanKorisnik: boolean = false;

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
        var role = localStorage.getItem('role');
        if(role == 'CUSTOMER'){
          this.ulogovanKorisnik = true;
          this.daLiJePretplacen(this.magazineId);
        }
      },
      (error) => {
        alert(error.message);
      }
    )
  }

  daLiJePretplacen(magazineId) {
    this.magazineService.checkIsSubbed(magazineId).subscribe(
      (res) => {
        var jelJe = res;
        if(jelJe === "Subbed") {
          this.pretplacen = true;
        }
      }, err => {
        alert("error pretplacen");
      }
    );
  }

  onKupi(){
    this.orderService.initMagazineOrder(this.magazine).subscribe(
      (response: any) => {
        window.location.href = response.redirectUrl;
      },
      (error) => {
        alert(error.message);
      }
    )
  }

  onPretplatise() {
    this.orderService.initMagazineSubscription(this.magazine).subscribe(
      (response: any) => {
        window.location.href = response.redirectUrl;
      },
      (error) => {
        alert(error.message);
      }
    );
  }

  onKupiRad(paper) {
    this.orderService.initPaperOrder(paper).subscribe(
      (response: any) => {
        window.location.href = response.redirectUrl;
      },
      (error) => {
        alert(error.message);
      }
    )
  }

}
