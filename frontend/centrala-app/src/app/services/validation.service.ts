import { Injectable } from '@angular/core';
import { NgForOf } from '@angular/common';
import { NgForm, Validators } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class ValidationService {

  controls: any = [];

  constructor() { }

  addValidations(formFields: any, form: NgForm){
    this.controls = form.controls;
    console.log(formFields);
    formFields.forEach( (field) => {
      console.log("POSTAVI VALIDACIJU: " + field.id);
      let controls = Object.keys(form.controls);
      console.log("CONTROLS: " + controls);
      Object.keys(form.controls).forEach((key) => {
        console.log("POSTAVI VALIDACIJU: " + form.controls[key].value);
        if(field.id == form.controls[key].value){
          for(var validator of field.validationConstraints){
            if(validator.name == 'required'){
              console.log("POSTAVI VALIDACIJU: " + form.controls[key].value);
              form.controls[key].setValidators(Validators.required);
              form.controls[key].updateValueAndValidity();
            }
          }    
        }
    });
    });
  }

}
