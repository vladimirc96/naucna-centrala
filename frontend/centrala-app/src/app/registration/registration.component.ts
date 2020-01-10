import { Component, OnInit, AfterViewInit, AfterContentChecked, AfterViewChecked } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import * as $ from 'jquery';
import { RepositoryService } from '../services/repository.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {
  

  formFieldsDto = null;
  formFields = [];
  enumValues = [];
  
  constructor(private repositoryService: RepositoryService, private userService: UserService) {
    
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

    let dto = new Array();

    for(var property in value){
      dto.push({fieldId: property, fieldValue: value[property]});
    }

    this.userService.registerUser(dto, this.formFieldsDto.taskId).subscribe(

      (response: any) => {
        alert(response.message);
        form.reset()
      },
      (error: any) => {
        alert(error.message);
      }
    )

  }

}
