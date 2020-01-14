import { Component, OnInit } from '@angular/core';
import { MagazineService } from 'src/app/services/magazine.service.';
import { ActivatedRoute, Params } from '@angular/router';

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

  constructor(private magazineService: MagazineService, private route: ActivatedRoute) {
    
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
      },
      (error) => {
        alert(error);
      }
    )

  }

}
