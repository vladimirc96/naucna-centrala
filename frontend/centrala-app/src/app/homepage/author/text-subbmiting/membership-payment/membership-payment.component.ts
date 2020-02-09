import { Component, OnInit } from '@angular/core';
import { MockService } from 'src/app/services/mock.service';
import { Route } from '@angular/compiler/src/core';
import { Router, ActivatedRoute, Params } from '@angular/router';

@Component({
  selector: 'app-membership-payment',
  templateUrl: './membership-payment.component.html',
  styleUrls: ['./membership-payment.component.css']
})
export class MembershipPaymentComponent implements OnInit {

  casopisi: any = [];
  formFieldsDto: any;
  formFields: any;
  processId: any;
  constructor(private mockService: MockService, private router: Router, private route: ActivatedRoute) {
    this.route.params.subscribe(
      (params: Params) => {
        this.processId = params['processId'];
      }
    )

    this.mockService.startPaymentProcess(this.processId).subscribe(
      (response: any) => {
        this.formFieldsDto = response;
        this.formFields = response.formFields;
        this.formFields.forEach( (field) =>{
          if( field.type.name=='enum'){
            this.casopisi = Object.keys(field.type.values);
          }
        });
      },
      (error) => { alert(error.message); }
    )
   }

  ngOnInit() {
  }

  onSubmit(value, form){
    
    let dto = new Array();

    for(var property in value){
          dto.push({fieldId: property, fieldValue: value[property]});
    }
    this.mockService.payment(this.formFieldsDto.taskId, dto).subscribe(
      (response: any) => {
        alert(response);
        this.router.navigate(['/homepage/author/text-subbmiting/science-paper-form/'.concat(this.processId)]);
      }, 
      (error) => {
        alert(error.message);
      }

    )


  }


}
