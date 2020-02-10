import { Component, OnInit } from '@angular/core';
import { SciencePaperService } from 'src/app/services/science-paper.service';
import { RepositoryService } from 'src/app/services/repository.service';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { ValidationService } from 'src/app/services/validation.service';

@Component({
  selector: 'app-chief-editor-choice',
  templateUrl: './chief-editor-choice.component.html',
  styleUrls: ['./chief-editor-choice.component.css']
})
export class ChiefEditorChoiceComponent implements OnInit {

  taskId: any;
  formFieldsDto = null;
  formFields = [];
  odluka = [];

  constructor(private sciencePaperService: SciencePaperService, private repoService: RepositoryService, private route: ActivatedRoute, private router: Router, private validationService: ValidationService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      }
    )

    this.repoService.getForm(this.taskId).subscribe(
      (response: any) => {
        this.formFieldsDto = response;
        this.formFields = response.formFields;
        this.formFields.forEach((field) => {
          if(field.type.name=='enum'){
            this.odluka = Object.keys(field.type.values);
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
    if(!this.validationService.validate(this.formFieldsDto.formFields, form)){
      return;
    }

    var dto = new Array();
    for(var property in value){
        dto.push({fieldId: property, fieldValue: value[property]});
    }

    this.sciencePaperService.chiefEditorChoice(this.taskId, dto).subscribe(
      (response) => {
        alert(response);
        this.router.navigate(['/homepage']);
      },
      (error) => {
        alert(error.message);
      }
    );
  }

}
