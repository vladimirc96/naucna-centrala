import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';
import { AdminService } from 'src/app/services/admin.service';
import { ActivatedRoute, Params, Router } from '@angular/router';

@Component({
  selector: 'app-check-magazine-data',
  templateUrl: './check-magazine-data.component.html',
  styleUrls: ['./check-magazine-data.component.css']
})
export class CheckMagazineDataComponent implements OnInit {
  
  taskId: any;

  formFieldsDto = null;
  formFields = [];
  naucneOblasti = [];
  urednici = [];
  recenzenti = [];

  correction: any;

  constructor(private repoService: RepositoryService, private adminService: AdminService, private route: ActivatedRoute, private router: Router) { 
    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      }
    )

    this.repoService.getCheckMagazineDataForm(this.taskId).subscribe(
      (response: any) => {
        this.formFieldsDto = response;
        this.formFields = response.formFields;
        
        this.formFields.forEach((field) => {
          if(field.type.name=='enum' && field.id == 'naucne_oblasti_provera'){
            this.naucneOblasti = Object.keys(field.type.values);
          }
          if(field.type.name=='enum' && field.id == 'urednici_provera'){
            this.urednici = Object.keys(field.type.values);
          }
          if(field.type.name=='enum' && field.id == 'recenzenti_provera'){
            this.recenzenti = Object.keys(field.type.values);
          }
        })

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
        this.router.navigate(['/homepage/admin/magazines']);
      },
      (error) => {
        alert(error.message);
      }
    )
  }

}
