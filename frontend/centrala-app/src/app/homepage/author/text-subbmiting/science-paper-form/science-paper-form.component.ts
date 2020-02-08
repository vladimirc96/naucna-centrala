import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { RepositoryService } from 'src/app/services/repository.service';
import { SciencePaperService } from 'src/app/services/science-paper.service';

@Component({
  selector: 'app-science-paper-form',
  templateUrl: './science-paper-form.component.html',
  styleUrls: ['./science-paper-form.component.css']
})
export class SciencePaperFormComponent implements OnInit {

  processId: any;
  formFieldsDto = null;
  formFields = [];
  fileUrl: string;
  fileToUpload: File;
  naucneOblasti: any = [];

  constructor(private route: ActivatedRoute, private repoService: RepositoryService, private sciencePaperService: SciencePaperService) {
    this.route.params.subscribe(
      (params: Params) => {
        this.processId = params['processId'];
      }
    )

    this.repoService.getSciencePaperForm(this.processId).subscribe(
      (response: any) => {
          this.formFieldsDto = response;
          this.formFields = response.formFields;
          this.formFields.forEach( (field) =>{
            if( field.type.name=='enum'){
              this.naucneOblasti = Object.keys(field.type.values);
            }
          });
      },
      (error) => {
        alert(error.message);
      }
    )
   }

  ngOnInit() {
  }

  handleFileInput(file:FileList){
    this.fileToUpload =file.item(0);
    var reader = new FileReader();
    reader.onload=(event:any)=>{
      this.fileUrl =event.target.result;
    }
    reader.readAsDataURL(this.fileToUpload);
    console.log("URL "+this.fileUrl);
    console.log("file "+this.fileToUpload);
}

  onSubmit(value, form){
    let dto = new Array();
    const formData = new FormData();
    for(var property in value){
      if(property == 'pdf'){
        continue;
      }    
      dto.push({fieldId: property, fieldValue: value[property]});
    }

    console.log(dto);

    this.sciencePaperService.save(this.formFieldsDto.taskId, dto).subscribe(
      (response: any) => {
        this.sciencePaperService.savePdf(response, this.fileToUpload).subscribe(
          (success) => {
            alert(success);
          }
        );
        alert(response);
      },
      (error) => {
        alert(error.message);
      }
    )

  }

}
