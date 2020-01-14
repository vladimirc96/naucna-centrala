import { Component, OnInit } from '@angular/core';
import { MagazineService } from 'src/app/services/magazine.service.';
import { Route } from '@angular/compiler/src/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-magazine-form',
  templateUrl: './magazine-form.component.html',
  styleUrls: ['./magazine-form.component.css']
})
export class MagazineFormComponent implements OnInit {

  formFieldsDto = null;
  formFields = [];
  nacinNaplacivanja = [];
  naucneOblasti = [];

  constructor(private magazineService: MagazineService, private router: Router) { 

    this.magazineService.getForm().subscribe(
      (response: any) => {
        this.formFieldsDto = response;
        this.formFields = response.formFields;
        this.formFields.forEach( (field) =>{
          if( field.type.name=='enum' && field.id == 'nacin_naplacivanja'){
            this.nacinNaplacivanja = Object.keys(field.type.values);
          }
          if( field.type.name=='enum' && field.id == 'naucne_oblasti'){
            this.naucneOblasti = Object.keys(field.type.values);
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
      if(property === 'naucne_oblasti'){
        var oblasti = value[property];
        for(let i=0; i<oblasti.length; i++){
          dto.push({fieldId: property, fieldValue: oblasti[i]});
        }
      }else{
        dto.push({fieldId: property, fieldValue: value[property]});
      }
    }

    this.magazineService.newMagazine(this.formFieldsDto.taskId, dto).subscribe(
      (response) => {
        alert(response);
        this.router.navigate(['/homepage/editor/editorial-board/' + this.formFieldsDto.processInstanceId]);
        console.log("PROCES ID: " + this.formFieldsDto.processInstanceId);
      },
      (error) => {
        alert(error.message);
        console.log("ERROR");
      }
    )


  }

}
