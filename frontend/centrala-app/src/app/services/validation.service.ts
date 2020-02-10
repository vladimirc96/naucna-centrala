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
    formFields.forEach( (field) => {
      console.log("POSTAVI VALIDACIJU: " + field.id);
          for(var validator of field.validationConstraints){
            if(validator.name == 'required'){
              console.log("POSTAVI VALIDACIJU: " + form.control[field.id]);
              form.form.get(field.id).setValidators(Validators.required);
              form.form.get(field.id).updateValueAndValidity();
            }
          }
      });
    }

    validate(formFields: any, form: NgForm){
      let isValid = true;
      
      formFields.forEach( (field) => {
      /* PROVERA REQUIRED VALIDACIJE */
      if(isValid == false){
        return;
      }
      
      for(var validator of field.validationConstraints){
              if(validator.name == 'required'){
                if(form.controls[field.id].value == '' || form.controls[field.id].value == null || form.controls[field.id].value == undefined){
                  isValid = false;
                  alert("Vrednost polja " + field.id + " ne sme biti prazno");
                }
              }
            }

      /* PROVERA MIN MAX */
        if(field.typeName == 'enum'){
          for(var prop in field.properties){
            console.log("PROPERTY: " + prop);
            console.log("PROPERTY VALUE: " + field.properties[prop]);
            if(prop == 'min'){
              
              var recenzentiTemp = new Array();
              for(var property in form.value){
                if(property === 'recenzenti'){
                  var recenzenti = form.value[property];
                  for(let i=0; i<recenzenti.length; i++){
                    recenzentiTemp.push({fieldId: property, fieldValue: recenzenti[i]});
                  }
                }
              }
              if(recenzentiTemp.length < field.properties[prop]){
                alert("Potrebno je postaviti minimalno " + field.properties[prop]  + " vrednosti za polje" + field.id + ".");
                isValid = false;
              }

            }
          }
        }
    });

      return isValid;
    }

}
