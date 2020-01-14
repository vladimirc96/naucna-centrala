import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';
import { AdminService } from 'src/app/services/admin.service';
import { Route, ActivatedRoute, Params, Router } from '@angular/router';

@Component({
  selector: 'app-reviewer-form',
  templateUrl: './reviewer-form.component.html',
  styleUrls: ['./reviewer-form.component.css']
})
export class ReviewerFormComponent implements OnInit {

  taskId: any;

  formFieldsDto = null;
  formFields = [];
  enumValues = [];

  naziv: string;
  prezime: string;
  controls: any = [];
  constructor(private adminService: AdminService, private route: ActivatedRoute, private router: Router) {

    this.route.params.subscribe(
      (params: Params) => {
        this.taskId = params['id'];
      }
    )


    this.adminService.getForm(this.taskId).subscribe(
      (response: any) => {
          this.formFieldsDto = response;
          this.formFields = response.formFields;
          this.formFields.forEach( (field) =>{
            if( field.type.name=='enum'){
              this.enumValues = Object.keys(field.type.values);
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

    this.adminService.setReviewer(dto, this.formFieldsDto.taskId).subscribe(
      (response) => {
        alert("Korsnik je postao recenzent!");
        this.router.navigate(['/admin']);
      },
      (error) => { alert(error.message) }
    )


  }

}
