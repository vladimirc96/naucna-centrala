import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';
import { AdminService } from 'src/app/services/admin.service';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { MagazineService } from 'src/app/services/magazine.service.';

@Component({
  selector: 'app-magazine-correction',
  templateUrl: './magazine-correction.component.html',
  styleUrls: ['./magazine-correction.component.css']
})
export class MagazineCorrectionComponent implements OnInit {

  taskId: any;
  
  formFieldsDto = null;
  formFields = [];
  enumValues = [];

  controls: any = [];
  billing: any;

  constructor(private repoService: RepositoryService, private route: ActivatedRoute, private router: Router, private magazineService: MagazineService) {
    
    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      }
    )
    this.magazineService.getMagazineCorrectionForm(this.taskId).subscribe(
      (response: any) => {
          this.formFieldsDto = response;
          this.formFields = response.formFields;
          this.formFields.forEach( (field) =>{
            if( field.type.name=='enum'){
              this.enumValues = Object.keys(field.type.values);
              console.log(this.enumValues);
              if(field.properties['Autorima'] === 'selected'){
                this.billing = 'autorima';
                console.log(this.billing);
              }else{
                this.billing = 'citaocima';
                console.log(this.billing);
              }

            }
          });

      },
      (error) => {alert(error)}
    )

   }

  ngOnInit() {
  }

  onSubmit(value, form){
    let dto = Array();

    this.controls = form.controls;
    for(var control in this.controls){
      dto.push({fieldId: control, fieldValue: this.controls[control].value});
    }
    console.log(dto);

    this.magazineService.magazineCorrection(dto, this.formFieldsDto.taskId).subscribe(
      (success) => {
        alert(success);
      },
      (error) => {
        alert(error);
      }
    )

  }

}
