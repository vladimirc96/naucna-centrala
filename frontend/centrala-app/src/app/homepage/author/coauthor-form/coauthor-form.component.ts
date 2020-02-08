import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';
import { SciencePaperService } from 'src/app/services/science-paper.service';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { CoauthorService } from 'src/app/services/coauthor.service';

@Component({
  selector: 'app-coauthor-form',
  templateUrl: './coauthor-form.component.html',
  styleUrls: ['./coauthor-form.component.css']
})
export class CoauthorFormComponent implements OnInit {
  
  taskId: any;
  formFieldsDto = null;
  formFields = [];

  constructor(private coauthorService: CoauthorService,private repoService: RepositoryService, private sciencePaperService: SciencePaperService,
     private route: ActivatedRoute, private router: Router) {

    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      }
    )

    this.repoService.getForm(this.taskId).subscribe(
      (response: any) => {
          this.formFieldsDto = response;
          this.formFields = response.formFields;
      },
      (error) => {
        alert(error.message);
      }
    )

   }

  ngOnInit() {
  }

  onSubmit(value, form){
    let dto = new Array();
    for(var property in value){
      dto.push({fieldId: property, fieldValue: value[property]});
    }

    this.coauthorService.save(this.taskId, dto).subscribe(
      (success) => {
        alert(success);
        this.router.navigate(['/homepage/author']);
      },
      (error) => {
        alert(error);
      }
    )

  }

}
