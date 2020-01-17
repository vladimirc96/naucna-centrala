import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';
import { AdminService } from 'src/app/services/admin.service';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { MagazineService } from 'src/app/services/magazine.service.';

@Component({
  selector: 'app-magazine-correction',
  templateUrl: './magazine-correction.component.html',
  styleUrls: ['./magazine-correction.component.css']
})
export class MagazineCorrectionComponent implements OnInit {

  taskId: any;
  
  formFieldsDto = null;
  formFields = [];
  enumValues = [];
  urednici = [];
  recenzenti = [];
  naucneOblasti = [];

  controls: any = [];
  billing: any;

  constructor(private repoService: RepositoryService, private route: ActivatedRoute, private router: Router, private magazineService: MagazineService) {
    
    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      }
    )
    this.magazineService.getMagazineCorrectionForm(this.taskId).subscribe(
      (response: any) => {
          this.formFieldsDto = response;
          this.formFields = response.formFields;
          this.formFields.forEach( (field) =>{
            if( field.type.name=='enum' && field.id == 'nacin_naplacivanja_stari'){
              this.enumValues = Object.keys(field.type.values);
              console.log(this.enumValues);
              if(field.properties['Autorima'] === 'selected'){
                this.billing = 'autorima';
                console.log(this.billing);
              }else{
                this.billing = 'citaocima';
                console.log(this.billing);
              }
            }

            if(field.type.name == 'enum' && field.id == 'naucne_oblasti_ispravka'){
              this.naucneOblasti = Object.keys(field.type.values);
            }
            if(field.type.name == 'enum' && field.id == 'urednici_ispravka'){
              this.urednici = Object.keys(field.type.values);
            }
            if(field.type.name == 'enum' && field.id == 'recenzenti_ispravka'){
              this.recenzenti = Object.keys(field.type.values);
            }

          });

      },
      (error) => {alert(error)}
    )

   }

  ngOnInit() {
  }

  onSubmit(value, form){

    if(!form.form.valid){
      alert("Potrebno popuniti sva polja oznacena sa zvezdicom.");
      return;
    }

    let dto = Array();

    this.controls = form.controls;
    for(var control in this.controls){
      if(control === 'naucne_oblasti_ispravka' || control === 'recenzenti_ispravka' || control === 'urednici_ispravka' ||
          control === 'naucne_oblasti_stare' || control === 'urednici_stari' || control === 'recenzenti_stari'){
        continue;
      }else{
        dto.push({fieldId: control, fieldValue: this.controls[control].value});
      }
    }

    for(var property in value){
      if(property === 'naucne_oblasti_ispravka'){
        var list = value[property];
        for(let i=0; i<list.length; i++){
          dto.push({fieldId: property, fieldValue: list[i]});
        }
      }
      if(property === 'urednici_ispravka'){
        var list = value[property];
        for(let i=0; i<list.length; i++){
          dto.push({fieldId: property, fieldValue: list[i]});
        }
      }
      if(property === 'recenzenti_ispravka'){
        var list = value[property];
        for(let i=0; i<list.length; i++){
          dto.push({fieldId: property, fieldValue: list[i]});
        }
      }
    }

    this.magazineService.magazineCorrection(dto, this.formFieldsDto.taskId).subscribe(
      (success) => {
        alert(success);
      },
      (error) => {
        alert(error);
      }
    )

  }

}
