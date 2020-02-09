import { Component, OnInit } from '@angular/core';
import { SciencePaperService } from 'src/app/services/science-paper.service';
import { RepositoryService } from 'src/app/services/repository.service';
import { ActivatedRoute, Router, Params } from '@angular/router';

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
  
  constructor(private sciencePaperService: SciencePaperService, private repoService: RepositoryService, private route: ActivatedRoute, private router: Router) { 
    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      }
    )

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
      }
    }

    

  }

}
