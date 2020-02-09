import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { SciencePaperService } from 'src/app/services/science-paper.service';

@Component({
  selector: 'app-review-paper',
  templateUrl: './review-paper.component.html',
  styleUrls: ['./review-paper.component.css']
})
export class ReviewPaperComponent implements OnInit {

  taskId: any;
  formFieldsDto = null;
  formFields = [];
  relevantnost = [];
  constructor(private sciencePaperService: SciencePaperService, private repoService: RepositoryService, private route: ActivatedRoute, private router: Router) {
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
            this.relevantnost = Object.keys(field.type.values);
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

    this.sciencePaperService.paperReview(this.taskId, dto).subscribe(
      (response) => {
        if(response == 'Rad je relevantan.'){
          this.router.navigate(['/homepage/editor/paper-format/'.concat(this.formFieldsDto.processInstanceId)]);
        }else{
          this.router.navigate(['/homepage']);
        }
      }
    )

  }

}
