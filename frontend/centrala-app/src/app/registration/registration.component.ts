import { Component, OnInit, AfterViewInit, AfterContentChecked, AfterViewChecked } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import * as $ from 'jquery';
import { RepositoryService } from '../services/repository.service';
import { UserService } from '../services/user.service';
import { Router, RouterModule } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  

  formFieldsDto = null;
  formFields = [];
  enumValues = [];

  isValid = true;
  
  constructor(private repositoryService: RepositoryService, private userService: UserService, private router: Router, private spinner: NgxSpinnerService) {
    
    repositoryService.startProcess().subscribe(

      (result: any) => {
        this.formFieldsDto = result;
        this.formFields = result.formFields;
        this.formFields.forEach((field) => {
          if(field.type.name=='enum'){
            this.enumValues = Object.keys(field.type.values);
          }
        })
      }
    )
   }

  ngOnInit() {
  }

  onSubmit(value, form){

    if(!form.form.valid){
      alert("Potrebno popuniti sva polja oznacena sa zvezdicom.");
      return;
    }

    let dto = new Array();

    for(var property in value){
      if(property === 'naucne_oblasti'){
        var oblasti = value[property];
        for(let i=0; i<oblasti.length; i++){
          dto.push({fieldId: property, fieldValue: oblasti[i]});
        }
      }else{
        dto.push({fieldId: property, fieldValue: value[property]});
      }
    }

    this.spinner.show();
    this.userService.registerUser(dto, this.formFieldsDto.taskId).subscribe(

      (response: any) => {
        this.spinner.hide();
        alert(response.message);
        form.reset();
        this.router.navigate(['/homepage']);
      },
      (error: any) => {
        alert(error.message);
      }
    )

  }


}
