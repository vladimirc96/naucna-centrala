import { Component, OnInit } from '@angular/core';
import { MagazineService } from 'src/app/services/magazine.service.';
import { ActivatedRoute, Params, Router } from '@angular/router';

@Component({
  selector: 'app-editorial-board-form',
  templateUrl: './editorial-board-form.component.html',
  styleUrls: ['./editorial-board-form.component.css']
})
export class EditorialBoardFormComponent implements OnInit {

  formFieldsDto = null;
  formFields = [];
  urednici = [];
  recenzenti = [];
  processInstanceId: string;

  constructor(private magazineService: MagazineService, private route: ActivatedRoute, private router: Router) {
    
    this.route.params.subscribe(
      (params: Params) => {
        this.processInstanceId = params['id'];
      }
    )

    this.magazineService.getEditorialBoardForm(this.processInstanceId).subscribe(
      (response: any) => {
        this.formFieldsDto = response;
        this.formFields = response.formFields;
        this.formFields.forEach( (field) =>{
          if( field.type.name=='enum' && field.id == 'recenzenti'){
            this.recenzenti = Object.keys(field.type.values);
          }
          if( field.type.name=='enum' && field.id == 'urednici'){
            this.urednici = Object.keys(field.type.values);
          }
        });
      },
      (error) => { alert(error.message); }
    )


   }

  ngOnInit() {
  }

  onSubmit(value, form){
    
    if(!this.validate(value,form)){
      return;
    }

    var dto = new Array();

    for(var property in value){
      if(property === 'recenzenti'){
        var recenzenti = value[property];
        for(let i=0; i<recenzenti.length; i++){
          dto.push({fieldId: property, fieldValue: recenzenti[i]});
        }
      }else if(property === 'urednici'){
        var urednici = value[property];
        for(let i=0; i<urednici.length; i++){
          dto.push({fieldId: property, fieldValue: urednici[i]});
        }
      }
    }

    this.magazineService.saveEditorialBoard(this.formFieldsDto.taskId, dto).subscribe(
      (success) => {
        alert(success);
        this.router.navigate(['/homepage/editor']);
      },
      (error) => {
        alert(error);
      }
    )

  }

  validate(value,form){
    if(!form.form.valid){
      alert("Potrebno je postaviti recenzente.");
      return false;
    }

    var recenzentiTemp = new Array();

    for(var property in value){
      if(property === 'recenzenti'){
        var recenzenti = value[property];
        for(let i=0; i<recenzenti.length; i++){
          recenzentiTemp.push({fieldId: property, fieldValue: recenzenti[i]});
        }
      }
    }

    if(recenzentiTemp.length < 2){
      alert("Potrebno je postaviti minimalno 2 recenzenta.");
      return false;
    }
    return true;
  }

}
