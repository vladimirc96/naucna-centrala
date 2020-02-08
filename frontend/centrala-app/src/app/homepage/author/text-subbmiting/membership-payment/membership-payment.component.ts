import { Component, OnInit } from '@angular/core';
import { MockService } from 'src/app/services/mock.service';
import { Route } from '@angular/compiler/src/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-membership-payment',
  templateUrl: './membership-payment.component.html',
  styleUrls: ['./membership-payment.component.css']
})
export class MembershipPaymentComponent implements OnInit {

  casopisi: any = [];
  formFieldsDto: any;
  formFields: any;

  constructor(private mockService: MockService, private router: Router) {
    this.mockService.startPaymentProcess().subscribe(
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
        this.router.navigate(['/homepage/author/text-subbmiting/science-paper-form']);
      }, 
      (error) => {
        alert(error.message);
      }

    )


  }


}
