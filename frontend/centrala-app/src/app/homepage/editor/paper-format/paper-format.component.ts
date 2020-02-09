import { Component, OnInit } from '@angular/core';
import { RepositoryService } from 'src/app/services/repository.service';
import { ActivatedRoute, Params } from '@angular/router';
import { SciencePaperService } from 'src/app/services/science-paper.service';

@Component({
  selector: 'app-paper-format',
  templateUrl: './paper-format.component.html',
  styleUrls: ['./paper-format.component.css']
})
export class PaperFormatComponent implements OnInit {

  processId: any;
  formFieldsDto = null;
  formFields = [];
  downloadUrl: any;
  formatiranost = [];

  constructor(private repoService: RepositoryService, private sciencePaperService: SciencePaperService, private route: ActivatedRoute) {     
      this.route.params.subscribe(
      (params: Params) => {
        this.processId = params['processId'];
        this.sciencePaperService.getPdfDownloadUrl(params['processId']).subscribe(
          (response) => {
            this.downloadUrl = response;
          },
          (error) => {
            alert(error.message);
          }
        )
      }
    )

    this.repoService.getPaperFormatForm(this.processId).subscribe(
      (response: any) => {
        this.formFieldsDto = response;
        this.formFields = response.formFields;
        this.formFields.forEach((field) => {
          if(field.type.name=='enum'){
            this.formatiranost = Object.keys(field.type.values);
          }
        })
      }
    )
}

  ngOnInit() {
  }


}
