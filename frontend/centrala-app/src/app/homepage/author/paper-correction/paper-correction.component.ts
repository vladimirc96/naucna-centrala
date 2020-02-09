import { Component, OnInit } from '@angular/core';
import { CoauthorService } from 'src/app/services/coauthor.service';
import { RepositoryService } from 'src/app/services/repository.service';
import { SciencePaperService } from 'src/app/services/science-paper.service';
import { ActivatedRoute, Router, Params } from '@angular/router';

@Component({
  selector: 'app-paper-correction',
  templateUrl: './paper-correction.component.html',
  styleUrls: ['./paper-correction.component.css']
})
export class PaperCorrectionComponent implements OnInit {
  taskId: any;
  formFieldsDto = null;
  formFields = [];

  fileUrl: string;
  fileToUpload: File;

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


  handleFileInput(file:FileList){
    this.fileToUpload =file.item(0);
    var reader = new FileReader();
    reader.onload=(event:any)=>{
    this.fileUrl =event.target.result;
    }
    reader.readAsDataURL(this.fileToUpload);
    console.log("URL "+this.fileUrl);
    console.log("file "+this.fileToUpload);
    console.log("filename " + this.fileToUpload.name);
  }

  ngOnInit() {
  }

  onSubmit(value, form){
    let dto = new Array();
    for(var property in value){
      if(property == 'pdf'){
        dto.push({fieldId: property, fieldValue: this.fileToUpload.name});  
      }
      dto.push({fieldId: property, fieldValue: value[property]});
    }

    this.sciencePaperService.paperCorrection(this.taskId, dto).subscribe(
      (response: any) => {
        this.sciencePaperService.savePdf(response, this.fileToUpload).subscribe(
          (response) => {
            alert(response);
          }
        ),
        (error) => {
          alert(error.message);
        }
      },
      (error) => { alert(error.message) }
    )

  }

}
