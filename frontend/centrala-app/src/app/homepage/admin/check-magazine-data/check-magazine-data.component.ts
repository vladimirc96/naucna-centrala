import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';
import { AdminService } from 'src/app/services/admin.service';
import { ActivatedRoute, Params } from '@angular/router';

@Component({
  selector: 'app-check-magazine-data',
  templateUrl: './check-magazine-data.component.html',
  styleUrls: ['./check-magazine-data.component.css']
})
export class CheckMagazineDataComponent implements OnInit {
  
  taskId: any;

  formFieldsDto = null;
  formFields = [];
  enumValues = [];
  correction: any;

  constructor(private repoService: RepositoryService, private adminService: AdminService, private route: ActivatedRoute) { 
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
    (error) => {alert(error)}
    )

  }

  ngOnInit() {
  }

  onSubmit(value, form){

    var dto = new Array();
    for(var property in value){
      if(property === 'ispravka'){
        dto.push({fieldId: property, fieldValue: value[property]});
      }
      if(property === 'komentar_uredniku'){
        dto.push({fieldId: property, fieldValue: value[property]});
      }    
    }

    this.adminService.checkMagazineDataSubmit(dto, this.formFieldsDto.taskId).subscribe(
      (success) => {
        alert(success);
      },
      (error) => {
        alert(error.message);
      }
    )
  }

}
