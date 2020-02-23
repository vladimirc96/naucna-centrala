import { Component, OnInit } from '@angular/core';
import { SciencePaperService } from 'src/app/services/science-paper.service';
import { RepositoryService } from 'src/app/services/repository.service';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { ValidationService } from 'src/app/services/validation.service';
import { SearchService } from 'src/app/services/search.service';

@Component({
  selector: 'app-choose-reviwers',
  templateUrl: './choose-reviwers.component.html',
  styleUrls: ['./choose-reviwers.component.css']
})
export class ChooseReviwersComponent implements OnInit {

  taskId: any;
  formFieldsDto = null;
  formFields = [];
  recenzenti = [];
  changed: boolean = false;

  constructor(private sciencePaperService: SciencePaperService, private repoService: RepositoryService, private route: ActivatedRoute, private router: Router,
     private validationService: ValidationService, private searchService: SearchService) { 
    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      }
    )

    this.getForm();
   }

  ngOnInit() {
  }

  refresh(){
    this.changed = false;
    this.getForm();
  }

  onSubmit(value, form){

    if(this.changed == false){
      return;
    }

    if(!this.validationService.validate(this.formFieldsDto.formFields, form)){
      return;
    }

    var dto = new Array();
    for(var property in value){
      if(property === 'recenzenti'){
        var recenzenti = value[property];
        for(let i=0; i<recenzenti.length; i++){
          dto.push({fieldId: property, fieldValue: recenzenti[i]});
        }
      }
      if(property == 'filterOption'){
        continue;
      }
    }

    this.sciencePaperService.chooseReviewers(this.taskId, dto).subscribe(
      (response: any) => {
        alert(response);
        this.router.navigate(['/homepage/editor']);
      },
      (error) => { alert(error.message) }
    )

  }

  onChange(value){
    console.log(value)
    if(value == "Recenzenti koji su recenzirali slične radove"){
      console.log("nista");
    }else if(value == "Recenzenti koji su udaljeni 100km od autora"){
      this.searchService.geoDistanceQuery(this.taskId).subscribe(
        (response: any) => {
          this.changed = true;
          this.recenzenti = response;
        }
      ),
      (error) => { alert(error.message) }
    }
  }

  getForm(){
    this.repoService.getChooseReviwersForm(this.taskId).subscribe(
      (response: any) => {
        this.formFieldsDto = response;
        this.formFields = response.formFields;
        this.formFields.forEach((field) => {
          if(field.type.name=='enum'){
            this.recenzenti = Object.keys(field.type.values);
          }
        })
      },
      (error) => {
        alert(error.message);
      }
    )
  }

}
