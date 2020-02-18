import { Component, OnInit } from '@angular/core';
import { KPService } from 'src/app/services/kp.service';

@Component({
  selector: 'app-subscriptions',
  templateUrl: './subscriptions.component.html',
  styleUrls: ['./subscriptions.component.css']
})
export class SubscriptionsComponent implements OnInit {

  agrList: any;
  emptyList: boolean = true;

  constructor(private kpServis: KPService) {
    this.kpServis.getUserAgreements().subscribe(
      res => {
        this.agrList = res;
        if(this.agrList.length != 0) {
          this.emptyList = false;
        }
      }, err => {
        alert("error getting subscriptions");
      }
    );
  }

  ngOnInit() {
  }

  onCancelAgreement(agrID, sellerID) {
    this.kpServis.cancelAgreement(agrID, sellerID).subscribe(
      res => {
        if(res === "done") {
          let i = this.agrList.findIndex(agr => agr.id === agrID);
          this.agrList.splice(i, 1);
          alert("Agreement cancelled!");
        }
      }, error => {
        alert("error canceling agreement");
      }
    );
  }


}
