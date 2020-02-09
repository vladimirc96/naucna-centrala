import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';
import { ActivatedRoute, Params } from '@angular/router';

@Component({
  selector: 'app-review-paper',
  templateUrl: './review-paper.component.html',
  styleUrls: ['./review-paper.component.css']
})
export class ReviewPaperComponent implements OnInit {

  taskId: any;
  formFieldsDto = null;
  formFields = [];
  relevantost = [];
  constructor(private repoService: RepositoryService, private route: ActivatedRoute) {
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
            this.relevantost = Object.keys(field.type.values);
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
        dto.push({fieldId: property, fieldValue: value[property]});
    }

  }

}
