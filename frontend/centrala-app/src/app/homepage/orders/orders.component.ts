import { Component, OnInit } from "@angular/core";
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: "app-orders",
  templateUrl: "./orders.component.html",
  styleUrls: ["./orders.component.css"]
})
export class OrdersComponent implements OnInit {


	orders: any[] = null;
	
	constructor(private ordersService: OrderService) {}

	ngOnInit() {
		this.ordersService.getAllOrders().subscribe(
			(res: any[]) => {
				this.orders = res;
			}, err => console.log(err.error)
		)
	}
}
