import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MagazineService } from 'src/app/services/magazine.service.';

@Component({
  selector: 'app-magazine-list',
  templateUrl: './magazine-list.component.html',
  styleUrls: ['./magazine-list.component.css']
})
export class MagazineListComponent implements OnInit {

  magazines: any[] = null;
	constructor(private router: Router, private magazineService: MagazineService) {
		this.magazineService.getAll().subscribe(
      (data: any) => {
        this.magazines = data;
      },
      (error) => {
        alert(error.message);
      }
    );
	}

	ngOnInit() {}

	// onBuy(r) {
	// 	this.centralaService.activeRad = r;
	// 	this.router.navigate(['/sellers', r.sellerId]);

	// }

	// onPlan(r) {
	// 	this.centralaService.activeRad = r;
	// 	this.router.navigate(['/paypal/plan']);
	// }
	
	// onRegister() {
	// 	this.sellersService.initRegister().subscribe(
	// 		(res: any) => {
	// 			this.router.navigate(['/reg/' + res.id]);
	// 		}, error => console.log(error.error)
	// 	)
	// }

	showMagazine(id){
		this.router.navigate(['/homepage/magazine/' + id]);
	}

}
