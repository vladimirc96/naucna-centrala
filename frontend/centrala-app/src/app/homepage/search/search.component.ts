import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { SearchService } from 'src/app/services/search.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  searchForm = new FormGroup({
    magazineName: new FormControl(""),
    title: new FormControl(""),
    imePrezimeAutora: new FormControl(""),
    keyTerms: new FormControl(""),
    text: new FormControl(""),
    scienceField: new FormControl("")
  });

  sciencePaperList: any = [];

  constructor(private searchService: SearchService) { }

  ngOnInit() {
  }


  onSubmit(value, form){

    var dto = new Array();
    dto = this.getFormFields(value);

    if(dto.length == 1){
      this.searchService.simpleQuery(dto[0]).subscribe(
        (response) => {
          this.sciencePaperList = response;
          console.log(this.sciencePaperList)
        },
        (error) => { alert(error.message) }
      )
    }else if(dto.length > 1){
      var booleanQuery =  {
        simpleQueryDTOList: dto,
        operation: "AND"
      }
      this.searchService.boolQuery(booleanQuery).subscribe(
        (response) => {
          this.sciencePaperList = response;
        },
        (error) => { alert(error.message) }
      )
    }

  }

  getFormFields(value){
    var list = new Array();
    for(var property in value){
      if(value[property] != ""){
        list.push({field: property, value: value[property]});
      }
    }
    return list;
  }


}
